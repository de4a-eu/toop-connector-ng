package eu.toop.connector.app.dd;

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
      final BDXRClientReadOnly aBDXR1Client = DDEndpointProviderSMP.getSMPClient (aParticipantID);

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
