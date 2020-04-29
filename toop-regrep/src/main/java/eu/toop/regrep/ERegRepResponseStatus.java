package eu.toop.regrep;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

public enum ERegRepResponseStatus
{
  SUCCESS ("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"),
  PARTIAL_SUCCESS ("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:PartialSuccess"),
  FAILURE ("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

  private final String m_sValue;

  private ERegRepResponseStatus (@Nonnull @Nonempty final String sValue)
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
