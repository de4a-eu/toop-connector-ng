package eu.toop.connector.api.dd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

/**
 * Helper interface to be used by the REST API.
 *
 * @author Philip Helger
 */
public interface IDDServiceMetadataProvider
{
  /**
   * @param aParticipantID
   *        Participant ID to query. May not be <code>null</code>.
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @return <code>null</code> if not found.
   */
  @Nullable
  ServiceMetadataType getServiceMetadata (@Nonnull IParticipantIdentifier aParticipantID, @Nonnull IDocumentTypeIdentifier aDocTypeID);
}
