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
package eu.toop.edm.xml.cccev;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.math.MathHelper;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cccev.CCCEVValueType;
import eu.toop.edm.jaxb.cv.cac.PeriodType;
import eu.toop.edm.jaxb.cv.cbc.AmountType;
import eu.toop.edm.jaxb.cv.cbc.CodeType;
import eu.toop.edm.jaxb.cv.cbc.DateType;
import eu.toop.edm.jaxb.cv.cbc.IDType;
import eu.toop.edm.jaxb.cv.cbc.IndicatorType;
import eu.toop.edm.jaxb.cv.cbc.MeasureType;
import eu.toop.edm.jaxb.cv.cbc.NumericType;
import eu.toop.edm.jaxb.cv.cbc.QuantityType;
import eu.toop.edm.jaxb.cv.cbc.TextType;
import eu.toop.edm.jaxb.cv.cbc.TimeType;
import eu.toop.edm.jaxb.cv.cbc.URIType;

@Immutable
public final class CCCEVValueHelper
{
  private CCCEVValueHelper ()
  {}

  @Nonnull
  public static CCCEVValueType createAmount (@Nullable final BigDecimal aValue, @Nullable final String sCurrencyID)
  {
    final AmountType a = new AmountType ();
    a.setValue (aValue);
    a.setCurrencyID (sCurrencyID);
    return create (a);
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final AmountType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setAmountValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createCode (@Nonnull final String sValue)
  {
    return create (new CodeType (sValue));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final CodeType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setCodeValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createDate (@Nonnull final LocalDate a)
  {
    return createDate (PDTXMLConverter.getXMLCalendarDate (a));
  }

  @Nonnull
  public static CCCEVValueType createDate (@Nonnull final XMLGregorianCalendar a)
  {
    return create (a == null ? null : new DateType (a));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final DateType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setDateValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createID (@Nonnull final String s)
  {
    return create (new IDType (s));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final IDType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setIdentifierValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createIndicator (final boolean b)
  {
    return createIndicator (Boolean.toString (b));
  }

  @Nonnull
  public static CCCEVValueType createIndicator (@Nonnull final String s)
  {
    final IndicatorType x = new IndicatorType ();
    x.setValue (s);
    return create (x);
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final IndicatorType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setIndicatorValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createMeasure (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
  {
    final MeasureType a = new MeasureType ();
    a.setValue (aValue);
    a.setUnitCode (sUnitCode);
    return create (a);
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final MeasureType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setMeasureValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createNumeric (@Nonnull final long n)
  {
    return createNumeric (MathHelper.toBigDecimal (n));
  }

  @Nonnull
  public static CCCEVValueType createNumeric (@Nonnull final double d)
  {
    return createNumeric (MathHelper.toBigDecimal (d));
  }

  @Nonnull
  public static CCCEVValueType createNumeric (@Nullable final BigDecimal a)
  {
    return create (new NumericType (a));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final NumericType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setNumericValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createQuantity (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
  {
    final QuantityType a = new QuantityType ();
    a.setValue (aValue);
    a.setUnitCode (sUnitCode);
    return create (a);
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final QuantityType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setQuantityValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createText (@Nonnull final String s)
  {
    return create (new TextType (s));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final TextType... a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    for (final TextType aItem : a)
      ret.getTextValue ().add (aItem);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final Collection <TextType> a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.getTextValue ().addAll (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createTime (@Nonnull final LocalTime a)
  {
    return createTime (PDTXMLConverter.getXMLCalendarTime (a));
  }

  @Nonnull
  public static CCCEVValueType createTime (@Nonnull final XMLGregorianCalendar a)
  {
    return create (a == null ? null : new TimeType (a));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final TimeType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setTimeValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createURI (@Nonnull final String s)
  {
    return create (new URIType (s));
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final URIType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setUriValue (a);
    return ret;
  }

  @Nonnull
  public static CCCEVValueType createPeriod (@Nonnull final LocalDateTime aStart, @Nonnull final LocalDateTime aEnd)
  {
    return createPeriod (aStart.toLocalDate (), aStart.toLocalTime (), aEnd.toLocalDate (), aEnd.toLocalTime ());
  }

  @Nonnull
  public static CCCEVValueType createPeriod (@Nullable final LocalDate aStartDate,
                                             @Nullable final LocalTime aStartTime,
                                             @Nullable final LocalDate aEndDate,
                                             @Nullable final LocalTime aEndTime)
  {
    return createPeriod (PDTXMLConverter.getXMLCalendarDate (aStartDate),
                         PDTXMLConverter.getXMLCalendarTime (aStartTime),
                         PDTXMLConverter.getXMLCalendarDate (aEndDate),
                         PDTXMLConverter.getXMLCalendarTime (aEndTime));
  }

  @Nonnull
  public static CCCEVValueType createPeriod (@Nonnull final XMLGregorianCalendar aStartDate,
                                             @Nonnull final XMLGregorianCalendar aStartTime,
                                             @Nonnull final XMLGregorianCalendar aEndDate,
                                             @Nonnull final XMLGregorianCalendar aEndTime)
  {
    final PeriodType a = new PeriodType ();
    if (aStartDate != null)
      a.setStartDate (aStartDate);
    if (aStartTime != null)
      a.setStartTime (aStartTime);
    if (aEndDate != null)
      a.setEndDate (aEndDate);
    if (aEndTime != null)
      a.setEndTime (aEndTime);
    return create (a);
  }

  @Nonnull
  public static CCCEVValueType create (@Nonnull final PeriodType a)
  {
    final CCCEVValueType ret = new CCCEVValueType ();
    ret.setPeriodValue (a);
    return ret;
  }
}
