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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.smpclient.bdxr1.BDXRClientReadOnly;
import com.helger.smpclient.exception.SMPClientException;
import com.helger.smpclient.url.PeppolDNSResolutionException;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;
import com.helger.xsds.bdxr.smp1.SignedServiceMetadataType;

import eu.toop.connector.api.dd.IDDServiceMetadataProvider;

/**
 * An implementation of {@link IDDServiceMetadataProvider} going to the SMP for
 * querying.
 *
 * @author Philip Helger
 */
public class DDServiceMetadataProviderSMP implements IDDServiceMetadataProvider
{
  public DDServiceMetadataProviderSMP ()
  {}

  @Nullable
  public ServiceMetadataType getServiceMetadata (@Nonnull final IParticipantIdentifier aParticipantID,
                                                 @Nonnull final IDocumentTypeIdentifier aDocTypeID)
  {
    try
    {
      final BDXRClientReadOnly aBDXR1Client = DDServiceGroupHrefProviderSMP.getSMPClient (aParticipantID);

      final SignedServiceMetadataType aSSM = aBDXR1Client.getServiceMetadataOrNull (aParticipantID, aDocTypeID);
      if (aSSM == null)
        return null;
      return aSSM.getServiceMetadata ();
    }
    catch (final PeppolDNSResolutionException | SMPClientException ex)
    {
      throw new IllegalStateException (ex);
    }
  }
}
