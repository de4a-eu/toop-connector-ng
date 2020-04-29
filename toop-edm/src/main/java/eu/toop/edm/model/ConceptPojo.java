package eu.toop.edm.model;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.cbc.IDType;

public class ConceptPojo
{
  private final String m_sID;
  private final QName m_aName;
  private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

  public ConceptPojo (@Nonnull final String sID,
                      @Nonnull final QName aName,
                      @Nullable final Iterable <ConceptPojo> aChildren)
  {
    m_sID = sID;
    m_aName = aName;
    m_aChildren.addAll (aChildren);
  }

  @Nonnull
  public CCCEVConceptType getAsCCCEVConcept ()
  {
    final CCCEVConceptType ret = new CCCEVConceptType ();
    if (StringHelper.hasText (m_sID))
    {
      final IDType aID = new IDType ();
      aID.setValue (m_sID);
      ret.addId (aID);
    }
    if (m_aName != null)
      ret.addQName (m_aName);
    for (final ConceptPojo aChild : m_aChildren)
      ret.addConcept (aChild.getAsCCCEVConcept ());
    return ret;
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  public static class Builder
  {
    private String m_sID;
    private QName m_aName;
    private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

    public Builder ()
    {}

    @Nonnull
    public Builder randomID ()
    {
      return id (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public Builder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nonnull
    public Builder name (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
    {
      return name (StringHelper.hasNoText (sLocalName) ? null : new QName (sNamespaceURI, sLocalName));
    }

    @Nonnull
    public Builder name (@Nullable final QName aName)
    {
      m_aName = aName;
      return this;
    }

    @Nonnull
    public Builder addChild (@Nullable final ConceptPojo.Builder a)
    {
      return addChild (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addChild (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aChildren.add (a);
      return this;
    }

    @Nonnull
    public ConceptPojo build ()
    {
      return new ConceptPojo (m_sID, m_aName, m_aChildren);
    }
  }
}
