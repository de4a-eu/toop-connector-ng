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
package eu.toop.connector.app.smp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.simple.process.SimpleProcessIdentifier;
import com.helger.smpclient.bdxr1.BDXRClientReadOnly;
import com.helger.smpclient.exception.SMPClientException;
import com.helger.smpclient.url.BDXLURLProvider;
import com.helger.smpclient.url.PeppolDNSResolutionException;
import com.helger.xsds.bdxr.smp1.EndpointType;
import com.helger.xsds.bdxr.smp1.ProcessType;
import com.helger.xsds.bdxr.smp1.ServiceInformationType;
import com.helger.xsds.bdxr.smp1.SignedServiceMetadataType;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.smp.ISMPEndpoint;
import eu.toop.connector.api.smp.ISMPEndpointProvider;
import eu.toop.connector.api.smp.ISMPErrorHandler;
import eu.toop.connector.api.smp.SMPEndpoint;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * The default implementation of {@link ISMPEndpointProvider} using the OASIS
 * BDXR SMP v1 lookup. It performs the query every time and does not cache
 * results!
 *
 * @author Philip Helger
 */
@Immutable
public class EndpointProviderBDXRSMP1 implements ISMPEndpointProvider
{
  public EndpointProviderBDXRSMP1 ()
  {}

  @Nonnull
  public static BDXRClientReadOnly getSMPClient (@Nonnull final IParticipantIdentifier aRecipientID) throws PeppolDNSResolutionException
  {
    if (TCConfig.R2D2.isR2D2UseDNS ())
    {
      // Use dynamic lookup via DNS - can throw exception
      return new BDXRClientReadOnly (BDXLURLProvider.INSTANCE, aRecipientID, TCConfig.R2D2.getR2D2SML ());
    }
    // Use a constant SMP URL
    return new BDXRClientReadOnly (TCConfig.R2D2.getR2D2SMPUrl ());
  }

  @Nullable
  public ISMPEndpoint getEndpoint (@Nonnull final String sLogPrefix,
                                   @Nonnull final IParticipantIdentifier aRecipientID,
                                   @Nonnull final IDocumentTypeIdentifier aDocumentTypeID,
                                   @Nonnull final IProcessIdentifier aProcessID,
                                   @Nonnull @Nonempty final String sTransportProfileID,
                                   @Nonnull final ISMPErrorHandler aErrorHandler)
  {
    ValueEnforcer.notNull (aRecipientID, "Recipient");
    ValueEnforcer.notNull (aDocumentTypeID, "DocumentTypeID");
    ValueEnforcer.notNull (aProcessID, "ProcessID");
    ValueEnforcer.notEmpty (sTransportProfileID, "TransportProfileID");
    ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");

    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> sLogPrefix +
                                "SMP lookup (" +
                                aRecipientID.getURIEncoded () +
                                ", " +
                                aDocumentTypeID.getURIEncoded () +
                                ", " +
                                aProcessID.getURIEncoded () +
                                ", " +
                                sTransportProfileID +
                                ")");

    try
    {
      final BDXRClientReadOnly aSMPClient = getSMPClient (aRecipientID);

      // Query SMP
      final SignedServiceMetadataType aSG = aSMPClient.getServiceMetadataOrNull (aRecipientID, aDocumentTypeID);
      final ServiceInformationType aSI = aSG == null ? null : aSG.getServiceMetadata ().getServiceInformation ();
      if (aSI != null)
      {
        // Find the first process that matches (should be only one!)
        final ProcessType aProcess = CollectionHelper.findFirst (aSI.getProcessList ().getProcess (),
                                                                 x -> SimpleProcessIdentifier.wrap (x.getProcessIdentifier ())
                                                                                             .hasSameContent (aProcessID));
        if (aProcess != null)
        {
          // Add all endpoints to the result list
          for (final EndpointType aEP : aProcess.getServiceEndpointList ().getEndpoint ())
            if (sTransportProfileID.equals (aEP.getTransportProfile ()))
            {
              // Convert String to X509Certificate
              final X509Certificate aCert = BDXRClientReadOnly.getEndpointCertificate (aEP);

              if (StringHelper.hasNoText (aEP.getEndpointURI ()))
              {
                ToopKafkaClient.send (EErrorLevel.WARN, () -> sLogPrefix + "SMP lookup result: endpoint has no URI");
                continue;
              }

              // Convert to our data structure
              final SMPEndpoint aDestEP = new SMPEndpoint (aRecipientID, aEP.getTransportProfile (), aEP.getEndpointURI (), aCert);

              ToopKafkaClient.send (EErrorLevel.INFO,
                                    () -> sLogPrefix + "SMP lookup result: " + aEP.getTransportProfile () + ", " + aEP.getEndpointURI ());

              return aDestEP;
            }
        }
      }
      else
      {
        // else redirect
        ToopKafkaClient.send (EErrorLevel.INFO, () -> sLogPrefix + "SMP lookup result: maybe a redirect?");
      }

      aErrorHandler.onWarning ("Endpoint lookup for '" +
                               aRecipientID.getURIEncoded () +
                               "' and document type ID '" +
                               aDocumentTypeID.getURIEncoded () +
                               "' and process ID '" +
                               aProcessID.getURIEncoded () +
                               "' and transport profile '" +
                               sTransportProfileID +
                               "' returned in no endpoints",
                               EToopErrorCode.DD_004);
    }
    catch (final PeppolDNSResolutionException | SMPClientException ex)
    {
      aErrorHandler.onError (sLogPrefix +
                             "Error fetching SMP endpoint " +
                             aRecipientID.getURIEncoded () +
                             "/" +
                             aDocumentTypeID.getURIEncoded () +
                             "/" +
                             aProcessID.getURIEncoded (),
                             ex,
                             EToopErrorCode.DD_002);
    }
    catch (final CertificateException ex)
    {
      aErrorHandler.onError (sLogPrefix +
                             "Error validating the signature from SMP response for endpoint " +
                             aRecipientID.getURIEncoded () +
                             "/" +
                             aDocumentTypeID.getURIEncoded () +
                             "/" +
                             aProcessID.getURIEncoded (),
                             ex,
                             EToopErrorCode.DD_003);
    }
    return null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).getToString ();
  }
}
