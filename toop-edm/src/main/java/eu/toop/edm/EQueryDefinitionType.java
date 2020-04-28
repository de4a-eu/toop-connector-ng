package eu.toop.edm;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * The RegRep Query "queryDefinition" value to use
 *
 * @author Philip Helger
 */
public enum EQueryDefinitionType
{
  CONCEPT ("ConceptQuery"),
  DOCUMENT ("DocumentQuery");

  private String m_sAttrValue;

  EQueryDefinitionType (@Nonnull @Nonempty final String sAttrValue)
  {
    m_sAttrValue = sAttrValue;
  }

  @Nonnull
  @Nonempty
  public String getAttrValue ()
  {
    return m_sAttrValue;
  }
}
