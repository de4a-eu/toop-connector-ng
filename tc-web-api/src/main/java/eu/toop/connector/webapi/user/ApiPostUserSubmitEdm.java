/**
 * Copyright (C) 2018-2020 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.connector.webapi.user;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.bdve.api.executorset.VESID;
import com.helger.bdve.api.result.ValidationResultList;
import com.helger.bdve.json.BDVEJsonHelper;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.mime.MimeTypeParser;
import com.helger.commons.string.StringHelper;
import com.helger.commons.timing.StopWatch;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.peppolid.simple.process.SimpleProcessIdentifier;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.security.certificate.CertificateHelper;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;
import com.helger.xsds.bdxr.smp1.EndpointType;
import com.helger.xsds.bdxr.smp1.ProcessType;
import com.helger.xsds.bdxr.smp1.ServiceInformationType;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

import eu.toop.connector.api.me.IMessageExchangeSPI;
import eu.toop.connector.api.me.MessageExchangeManager;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.me.outgoing.MERoutingInformation;
import eu.toop.connector.api.me.outgoing.MERoutingInformationInput;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.connector.app.validation.TCValidator;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.ETCEdmType;
import eu.toop.connector.webapi.TCAPIConfig;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;
import eu.toop.connector.webapi.smp.SMPJsonResponse;

/**
 * Perform validation, lookup and sending via API
 *
 * @author Philip Helger
 */
public class ApiPostUserSubmitEdm extends AbstractTCAPIInvoker
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ApiPostUserSubmitEdm.class);

  private final ETCEdmType m_eType;

  public ApiPostUserSubmitEdm (@Nonnull final ETCEdmType eType)
  {
    m_eType = eType;
  }

  @Override
  public IJsonObject invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                                @Nonnull @Nonempty final String sPath,
                                @Nonnull final Map <String, String> aPathVariables,
                                @Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws IOException
  {
    // Read the payload as XML
    final TCOutgoingMessage aOutgoingMsg = TCRestJAXB.outgoingMessage ().read (aRequestScope.getRequest ().getInputStream ());
    if (aOutgoingMsg == null)
      throw new APIParamException ("Failed to interpret the message body as an 'OutgoingMessage'");

    // These fields MUST not be present here - they are filled while we go
    if (StringHelper.hasText (aOutgoingMsg.getMetadata ().getEndpointURL ()))
      throw new APIParamException ("The 'OutgoingMessage/Metadata/EndpointURL' element MUST NOT be present");
    if (ArrayHelper.isNotEmpty (aOutgoingMsg.getMetadata ().getReceiverCertificate ()))
      throw new APIParamException ("The 'OutgoingMessage/Metadata/ReceiverCertificate' element MUST NOT be present");

    // Convert metadata
    final MERoutingInformationInput aRoutingInfo = MERoutingInformationInput.createForInput (aOutgoingMsg.getMetadata ());

    final Locale aDisplayLocale = Locale.UK;

    final IJsonObject aJson = new JsonObject ();
    {
      aJson.add ("senderid", aRoutingInfo.getSenderID ().getURIEncoded ());
      aJson.add ("receiverid", aRoutingInfo.getReceiverID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_DOCUMENT_TYPE_ID, aRoutingInfo.getDocumentTypeID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_PROCESS_ID, aRoutingInfo.getProcessID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_TRANSPORT_PROFILE, aRoutingInfo.getTransportProtocol ());
    }

    CommonAPIInvoker.invoke (aJson, () -> {
      final boolean bValidationOK;
      boolean bOverallSuccess = false;
      {
        // validation
        final StopWatch aSW = StopWatch.createdStarted ();
        final VESID aVESID = m_eType.getVESID ();
        final ValidationResultList aValidationResultList = TCAPIConfig.getVSValidator ()
                                                                      .validate (aVESID,
                                                                                 aOutgoingMsg.getPayloadAtIndex (0).getValue (),
                                                                                 aDisplayLocale);
        aSW.stop ();

        final IJsonObject aJsonVR = new JsonObject ();
        BDVEJsonHelper.applyValidationResultList (aJsonVR,
                                                  TCValidator.getVES (aVESID),
                                                  aValidationResultList,
                                                  aDisplayLocale,
                                                  aSW.getMillis (),
                                                  null,
                                                  null);
        aJson.addJson ("validation-results", aJsonVR);

        bValidationOK = aValidationResultList.containsNoError ();
      }

      if (bValidationOK)
      {
        MERoutingInformation aRoutingInfoFinal = null;
        final IJsonObject aJsonSMP = new JsonObject ();
        // Main query
        final ServiceMetadataType aSM = TCAPIConfig.getDDServiceMetadataProvider ()
                                                   .getServiceMetadata (aRoutingInfo.getReceiverID (), aRoutingInfo.getDocumentTypeID ());
        if (aSM != null)
        {
          aJsonSMP.addJson ("response", SMPJsonResponse.convert (aRoutingInfo.getReceiverID (), aRoutingInfo.getDocumentTypeID (), aSM));

          final ServiceInformationType aSI = aSM.getServiceInformation ();
          if (aSI != null)
          {
            final ProcessType aProc = CollectionHelper.findFirst (aSI.getProcessList ().getProcess (),
                                                                  x -> aRoutingInfo.getProcessID ()
                                                                                   .hasSameContent (SimpleProcessIdentifier.wrap (x.getProcessIdentifier ())));
            if (aProc != null)
            {
              final EndpointType aEndpoint = CollectionHelper.findFirst (aProc.getServiceEndpointList ().getEndpoint (),
                                                                         x -> aRoutingInfo.getTransportProtocol ()
                                                                                          .equals (x.getTransportProfile ()));
              if (aEndpoint != null)
              {
                aJsonSMP.add (SMPJsonResponse.JSON_ENDPOINT_REFERENCE, aEndpoint.getEndpointURI ());
                aRoutingInfoFinal = new MERoutingInformation (aRoutingInfo,
                                                              aEndpoint.getEndpointURI (),
                                                              CertificateHelper.convertByteArrayToCertficateDirect (aEndpoint.getCertificate ()));
              }
            }
          }
          if (aRoutingInfoFinal == null)
          {
            LOGGER.warn ("[API] The SMP lookup for '" +
                         aRoutingInfo.getReceiverID ().getURIEncoded () +
                         "' and '" +
                         aRoutingInfo.getDocumentTypeID ().getURIEncoded () +
                         "' succeeded, but no endpoint matching '" +
                         aRoutingInfo.getProcessID ().getURIEncoded () +
                         "' and '" +
                         aRoutingInfo.getTransportProtocol () +
                         "' was found.");
          }

          // Only if a match was found
          aJsonSMP.add (JSON_SUCCESS, aRoutingInfoFinal != null);
        }
        else
          aJsonSMP.add (JSON_SUCCESS, false);
        aJson.addJson ("lookup-results", aJsonSMP);

        // Read for sending?
        if (aRoutingInfoFinal != null)
        {
          final IJsonObject aJsonSending = new JsonObject ();
          final IMessageExchangeSPI aMEM = MessageExchangeManager.getConfiguredImplementation ();

          // Add payloads
          final MEMessage.Builder aMessage = MEMessage.builder ();
          for (final TCPayload aPayload : aOutgoingMsg.getPayload ())
          {
            aMessage.addPayload (MEPayload.builder ()
                                          .mimeType (MimeTypeParser.parseMimeType (aPayload.getMimeType ()))
                                          .contentID (StringHelper.getNotEmpty (aPayload.getContentID (),
                                                                                MEPayload.createRandomContentID ()))
                                          .data (aPayload.getValue ()));
          }
          aMEM.sendOutgoing (aRoutingInfoFinal, aMessage.build ());
          aJsonSending.add (JSON_SUCCESS, true);

          aJson.addJson ("sending-results", aJsonSending);
          bOverallSuccess = true;
        }

        // Overall success
        aJson.add (JSON_SUCCESS, bOverallSuccess);
      }
    });

    return aJson;
  }
}
