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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.math.MathHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cv.cbc.QuantityType;

/**
 * Representation of an "Quantity" value.
 *
 * @author Philip Helger
 */
public class QuantityPojo
{
  private final BigDecimal m_aValue;
  private final String m_sUnitCode;

  public QuantityPojo (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
  {
    m_aValue = aValue;
    m_sUnitCode = sUnitCode;
  }

  @Nullable
  public BigDecimal getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public String getUnitCode ()
  {
    return m_sUnitCode;
  }

  @Nonnull
  public QuantityType getAsQuantity ()
  {
    final QuantityType ret = new QuantityType ();
    ret.setValue (m_aValue);
    ret.setUnitCode (m_sUnitCode);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return false;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final QuantityPojo rhs = (QuantityPojo) o;
    return EqualsHelper.equals (m_aValue, rhs.m_aValue) && EqualsHelper.equals (m_sUnitCode, rhs.m_sUnitCode);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_sUnitCode).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Value", m_aValue).append ("UnitCode", m_sUnitCode).getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final QuantityType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
      ret.value (a.getValue ()).unitCode (a.getUnitCode ());
    return ret;
  }

  public static class Builder
  {
    private BigDecimal m_aValue;
    private String m_sUnitCode;

    public Builder ()
    {}

    public Builder value (@Nonnull final long n)
    {
      return value (MathHelper.toBigDecimal (n));
    }

    @Nonnull
    public Builder value (@Nonnull final double d)
    {
      return value (MathHelper.toBigDecimal (d));
    }

    @Nonnull
    public Builder value (@Nullable final BigDecimal a)
    {
      m_aValue = a;
      return this;
    }

    @Nonnull
    public Builder unitCode (@Nullable final String s)
    {
      m_sUnitCode = s;
      return this;
    }

    @Nonnull
    public QuantityPojo build ()
    {
      return new QuantityPojo (m_aValue, m_sUnitCode);
    }
  }
}
