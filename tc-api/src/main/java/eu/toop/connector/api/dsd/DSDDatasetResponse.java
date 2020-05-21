package eu.toop.connector.api.dsd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;

/**
 * Simplified DSD Response Representation
 *
 * @author jerouris at 21.05.2020
 */
public class DSDDatasetResponse
{
  private IParticipantIdentifier m_aDPIdentifier;

  private String m_sDatasetIdentifier;

  private String m_sDistributionFormat;
  private String m_sDistributionConforms;
  private String m_sDistributionMediaType;

  private String m_sAccessServiceConforms;

  private IDocumentTypeIdentifier m_aDocumentTypeIdentifier;

  public DSDDatasetResponse ()
  {}

  @Nullable
  public IParticipantIdentifier getDPIdentifier ()
  {
    return m_aDPIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDPIdentifier (@Nullable final IParticipantIdentifier aDPIdentifier)
  {
    m_aDPIdentifier = aDPIdentifier;
    return this;
  }

  @Nullable
  public String getDatasetIdentifier ()
  {
    return m_sDatasetIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDatasetIdentifier (@Nullable final String sDatasetIdentifier)
  {
    m_sDatasetIdentifier = sDatasetIdentifier;
    return this;
  }

  @Nullable
  public String getDistributionFormat ()
  {
    return m_sDistributionFormat;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionFormat (@Nullable final String sDistributionFormat)
  {
    m_sDistributionFormat = sDistributionFormat;
    return this;
  }

  @Nullable
  public String getDistributionConforms ()
  {
    return m_sDistributionConforms;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionConforms (@Nullable final String sDistributionConforms)
  {
    m_sDistributionConforms = sDistributionConforms;
    return this;
  }

  @Nullable
  public String getDistributionMediaType ()
  {
    return m_sDistributionMediaType;
  }

  @Nonnull
  public DSDDatasetResponse setDistributionMediaType (@Nullable final String sDistributionMediaType)
  {
    m_sDistributionMediaType = sDistributionMediaType;
    return this;
  }

  @Nullable
  public String getAccessServiceConforms ()
  {
    return m_sAccessServiceConforms;
  }

  @Nonnull
  public DSDDatasetResponse setAccessServiceConforms (@Nullable final String sAccessServiceConforms)
  {
    m_sAccessServiceConforms = sAccessServiceConforms;
    return this;
  }

  @Nullable
  public IDocumentTypeIdentifier getDocumentTypeIdentifier ()
  {
    return m_aDocumentTypeIdentifier;
  }

  @Nonnull
  public DSDDatasetResponse setDocumentTypeIdentifier (@Nullable final IDocumentTypeIdentifier aDocumentTypeIdentifier)
  {
    m_aDocumentTypeIdentifier = aDocumentTypeIdentifier;
    return this;
  }
}
