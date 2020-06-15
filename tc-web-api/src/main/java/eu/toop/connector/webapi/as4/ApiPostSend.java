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
package eu.toop.connector.webapi.as4;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.mime.MimeTypeParser;
import com.helger.commons.string.StringHelper;
import com.helger.json.IJsonObject;
import com.helger.json.JsonObject;
import com.helger.photon.api.IAPIDescriptor;
import com.helger.photon.app.PhotonUnifiedResponse;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

import eu.toop.connector.api.me.IMessageExchangeSPI;
import eu.toop.connector.api.me.MessageExchangeManager;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MERoutingInformation;
import eu.toop.connector.api.rest.TCOutgoingMessage;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCRestJAXB;
import eu.toop.connector.webapi.APIParamException;
import eu.toop.connector.webapi.dd.SMPJsonResponse;
import eu.toop.connector.webapi.helper.AbstractTCAPIInvoker;
import eu.toop.connector.webapi.helper.CommonAPIInvoker;

/**
 * Send an outgoing AS4 message via the configured MEM gateway
 *
 * @author Philip Helger
 */
public class ApiPostSend extends AbstractTCAPIInvoker
{
  @Override
  public void invokeAPI (@Nonnull final IAPIDescriptor aAPIDescriptor,
                         @Nonnull @Nonempty final String sPath,
                         @Nonnull final Map <String, String> aPathVariables,
                         @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                         @Nonnull final PhotonUnifiedResponse aUnifiedResponse) throws IOException
  {
    // Read the payload as XML
    final TCOutgoingMessage aOutgoingMsg = TCRestJAXB.outgoingMessage ().read (aRequestScope.getRequest ().getInputStream ());
    if (aOutgoingMsg == null)
      throw new APIParamException ("Failed to interpret the message body as an 'OutgoingMessage'");

    // These fields are optional in the XSD but required here
    if (StringHelper.hasNoText (aOutgoingMsg.getMetadata ().getEndpointURL ()))
      throw new APIParamException ("The 'OutgoingMessage/Metadata/EndpointURL' element MUST be present and not empty");
    if (ArrayHelper.isEmpty (aOutgoingMsg.getMetadata ().getReceiverCertificate ()))
      throw new APIParamException ("The 'OutgoingMessage/Metadata/ReceiverCertificate' element MUST be present and not empty");

    // Convert metadata
    final IMERoutingInformation aRoutingInfo;
    try
    {
      aRoutingInfo = MERoutingInformation.createFrom (aOutgoingMsg.getMetadata ());
    }
    catch (final CertificateException ex)
    {
      throw new APIParamException ("Invalid certificate provided: " + ex.getMessage ());
    }

    // Add payloads
    final MEMessage.Builder aMessage = MEMessage.builder ();
    for (final TCPayload aPayload : aOutgoingMsg.getPayload ())
    {
      aMessage.addPayload (MEPayload.builder ()
                                    .mimeType (MimeTypeParser.parseMimeType (aPayload.getMimeType ()))
                                    .contentID (StringHelper.getNotEmpty (aPayload.getContentID (), MEPayload.createRandomContentID ()))
                                    .data (aPayload.getValue ()));
    }

    final IJsonObject aJson = new JsonObject ();
    {
      aJson.add ("senderid", aRoutingInfo.getSenderID ().getURIEncoded ());
      aJson.add ("receiverid", aRoutingInfo.getReceiverID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_DOCUMENT_TYPE_ID, aRoutingInfo.getDocumentTypeID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_PROCESS_ID, aRoutingInfo.getProcessID ().getURIEncoded ());
      aJson.add (SMPJsonResponse.JSON_TRANSPORT_PROFILE, aRoutingInfo.getTransportProtocol ());
      aJson.add (SMPJsonResponse.JSON_ENDPOINT_REFERENCE, aRoutingInfo.getEndpointURL ());
    }

    CommonAPIInvoker.invoke (aJson, () -> {
      final IMessageExchangeSPI aMEM = MessageExchangeManager.getConfiguredImplementation ();
      aMEM.sendOutgoing (aRoutingInfo, aMessage.build ());
      aJson.add ("success", true);
    });

    aUnifiedResponse.json (aJson);
  }
}
