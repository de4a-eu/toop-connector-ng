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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.error.IToopErrorCode;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.cbc.IDType;

public class ConceptPojo
{
  private final String m_sID;
  private final QName m_aName;
  private final ConceptValuePojo m_aValue;
  private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

  public ConceptPojo (@Nullable final String sID,
                      @Nullable final QName aName,
                      @Nullable final ConceptValuePojo aValue,
                      @Nullable final List <ConceptPojo> aChildren)
  {
    m_sID = sID;
    m_aName = aName;
    m_aValue = aValue;
    if (aChildren != null)
      m_aChildren.addAll (aChildren);
  }

  @Nullable
  public final String getID ()
  {
    return m_sID;
  }

  @Nullable
  public final QName getName ()
  {
    return m_aName;
  }

  @Nullable
  public final ConceptValuePojo getValue ()
  {
    return m_aValue;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <ConceptPojo> children ()
  {
    return m_aChildren;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ConceptPojo> getAllChildren ()
  {
    return m_aChildren.getClone ();
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
      ret.addValue (m_aValue.getAsCCCEVValueType ());
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
    private ConceptValuePojo m_aValue;
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
    public Builder name (@Nullable final IConceptName aConcept)
    {
      return name (aConcept == null ? null : aConcept.getAsQName ());
    }

    @Nonnull
    public Builder name (@Nullable final QName a)
    {
      m_aName = a;
      return this;
    }

    @Nonnull
    public Builder valueID (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().identifier (s));
    }

    @Nonnull
    public Builder valueCode (@Nullable final AmountPojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().amount (a));
    }

    @Nonnull
    public Builder valueCode (@Nullable final AmountPojo a)
    {
      return value (ConceptValuePojo.builder ().amount (a));
    }

    @Nonnull
    public Builder valueAmount (@Nullable final BigDecimal aValue, @Nullable final String sCurrencyID)
    {
      return value (ConceptValuePojo.builder ().amount (aValue, sCurrencyID));
    }

    @Nonnull
    public Builder valueCode (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().code (s));
    }

    @Nonnull
    public Builder valueDate (@Nullable final LocalDate a)
    {
      return value (ConceptValuePojo.builder ().date (a));
    }

    @Nonnull
    public Builder valueIndicator (final boolean b)
    {
      return value (ConceptValuePojo.builder ().indicator (b));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final MeasurePojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().measure (a));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final MeasurePojo a)
    {
      return value (ConceptValuePojo.builder ().measure (a));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return value (ConceptValuePojo.builder ().measure (aValue, sUnitCode));
    }

    @Nonnull
    public Builder valueNumeric (@Nonnull final long n)
    {
      return value (ConceptValuePojo.builder ().numeric (n));
    }

    @Nonnull
    public Builder valueNumeric (@Nonnull final double d)
    {
      return value (ConceptValuePojo.builder ().numeric (d));
    }

    @Nonnull
    public Builder valueNumeric (@Nullable final BigDecimal a)
    {
      return value (ConceptValuePojo.builder ().numeric (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final QuantityPojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().quantity (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final QuantityPojo a)
    {
      return value (ConceptValuePojo.builder ().quantity (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return value (ConceptValuePojo.builder ().quantity (aValue, sUnitCode));
    }

    @Nonnull
    public Builder valueText (@Nonnull final String s)
    {
      return value (ConceptValuePojo.builder ().text (s));
    }

    @Nonnull
    public Builder valueTime (@Nullable final LocalTime a)
    {
      return value (ConceptValuePojo.builder ().time (a));
    }

    @Nonnull
    public Builder valueURI (@Nonnull final String s)
    {
      return value (ConceptValuePojo.builder ().uri (s));
    }

    @Nonnull
    public Builder valueErrorCode (@Nullable final IToopErrorCode a)
    {
      return value (ConceptValuePojo.builder ().errorCode (a));
    }

    @Nonnull
    public Builder valueErrorCode (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().errorCode (s));
    }

    @Nonnull
    public Builder value (@Nullable final ConceptValuePojo.Builder a)
    {
      return value (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder value (@Nullable final ConceptValuePojo a)
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
    public Builder child (@Nullable final ConceptPojo.Builder a)
    {
      return child (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder child (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aChildren.set (a);
      else
        m_aChildren.clear ();
      return this;
    }

    @Nonnull
    public Builder children (@Nullable final ConceptPojo... a)
    {
      m_aChildren.setAll (a);
      return this;
    }

    @Nonnull
    public Builder children (@Nullable final Iterable <? extends ConceptPojo> a)
    {
      m_aChildren.setAll (a);
      return this;
    }

    @Nonnull
    public ConceptPojo build ()
    {
      return new ConceptPojo (m_sID, m_aName, m_aValue, m_aChildren);
    }
  }
}
