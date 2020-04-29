package eu.toop.edm.model;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

public enum EDistributionFormat
{
  STRUCTURED ("STRUCTURED"),
  UNSTRUCTURED ("UNSTRUCTURED");

  private final String m_sValue;

  EDistributionFormat (@Nonnull @Nonempty final String sValue)
  {
    m_sValue = sValue;
  }

  @Nonnull
  @Nonempty
  public String getValue ()
  {
    return m_sValue;
  }

}
