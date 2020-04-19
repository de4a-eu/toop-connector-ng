package eu.toop.regrep;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Enumeration with collection types.
 * 
 * @author Philip Helger
 */
public enum ECollectionType
{
  BAG ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Bag"),
  LIST ("urn:oasis:names:tc:ebxml-regrep:CollectionType:List"),
  SET ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Set"),
  SORTED_SET ("urn:oasis:names:tc:ebxml-regrep:CollectionType:Set:SortedSet");

  private final String m_sValue;

  private ECollectionType (@Nonnull @Nonempty final String sValue)
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
