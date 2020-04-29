package eu.toop.edm.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcterms.DCMediaType;

public class DistributionPojo
{
  private final EDistributionFormat m_eFormat;
  private final String m_sMediaType;

  public DistributionPojo (@Nullable final EDistributionFormat eFormat, @Nullable final String sMediaType)
  {
    m_eFormat = eFormat;
    m_sMediaType = sMediaType;
  }

  @Nonnull
  public DCatAPDistributionType getAsDistribution ()
  {
    final DCatAPDistributionType ret = new DCatAPDistributionType ();
    // Mandatory element but not needed atm
    ret.setAccessURL ("");
    if (m_eFormat != null)
    {
      final DCMediaType aFormat = new DCMediaType ();
      aFormat.addContent (m_eFormat.getValue ());
      ret.setFormat (aFormat);
    }
    if (StringHelper.hasText (m_sMediaType))
    {
      final DCMediaType aMediaType = new DCMediaType ();
      aMediaType.addContent (m_sMediaType);
      ret.setMediaType (aMediaType);
    }
    return ret;
  }
}
