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
package eu.toop.edm.model;

import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cccev.CCCEVValueType;
import eu.toop.edm.jaxb.cv.cbc.IDType;

public class ConceptPojo
{
  private final String m_sID;
  private final QName m_aName;
  private final CCCEVValueType m_aValue;
  private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

  public ConceptPojo (@Nullable final String sID,
                      @Nullable final QName aName,
                      @Nullable final CCCEVValueType aValue,
                      @Nullable final Iterable <ConceptPojo> aChildren)
  {
    m_sID = sID;
    m_aName = aName;
    m_aValue = aValue;
    m_aChildren.addAll (aChildren);
  }

  public void visitRecursive (@Nonnull final Consumer <? super ConceptPojo> aConsumer)
  {
    // Invoke
    aConsumer.accept (this);

    // For all children
    for (final ConceptPojo aItem : m_aChildren)
      aItem.visitRecursive (aConsumer);
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
    if (m_aValue != null)
      ret.addValue (m_aValue);
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
    private CCCEVValueType m_aValue;
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
    public Builder name (@Nullable final QName a)
    {
      m_aName = a;
      return this;
    }

    @Nonnull
    public Builder value (@Nullable final CCCEVValueType a)
    {
      m_aValue = a;
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
      return new ConceptPojo (m_sID, m_aName, m_aValue, m_aChildren);
    }
  }
}
