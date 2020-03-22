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
package eu.toop.commons.jaxb;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.mime.IMimeType;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.dataexchange.v140.TDECodeWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEDateWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEIdentifierWithLOAType;
import eu.toop.commons.dataexchange.v140.TDELOAtype;
import eu.toop.commons.dataexchange.v140.TDEPreferredDocumentMimeTypeCodeType;
import eu.toop.commons.dataexchange.v140.TDETextWithLOAType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.CodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IndicatorType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.NumericType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

/**
 * A helper class to created basic data model 1.4.0 XSD compliant constructs.
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public final class ToopXSDHelper140
{
  private ToopXSDHelper140 ()
  {}

  @Nonnull
  private static TDELOAtype _getLOAValue (@Nullable final EToopLevelOfAssurance eLOA)
  {
    return eLOA != null ? TDELOAtype.fromValue (eLOA.getID ()) : TDELOAtype.NONE;
  }

  @Nonnull
  public static IdentifierType createIdentifier (@Nonnull @Nonempty final String sValue)
  {
    final IdentifierType ret = new IdentifierType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static IdentifierType createIdentifier (@Nullable final String sSchemeID,
                                                 @Nonnull @Nonempty final String sValue)
  {
    final IdentifierType ret = new IdentifierType ();
    ret.setSchemeID (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static IdentifierType createIdentifier (@Nullable final String sSchemeAgencyID,
                                                 @Nullable final String sSchemeID,
                                                 @Nonnull @Nonempty final String sValue)
  {
    final IdentifierType ret = new IdentifierType ();
    ret.setSchemeAgencyID (sSchemeAgencyID);
    ret.setSchemeID (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static IdentifierType createSpecificationIdentifierResponse ()
  {
    return createIdentifier (EPredefinedDocumentTypeIdentifier.DOC_TYPE_SCHEME,
                             "urn:eu:toop:ns:dataexchange-1p40::Response");
  }

  @Nonnull
  public static IdentifierType createIdentifierUUID ()
  {
    return createIdentifier ("UUID", null, UUID.randomUUID ().toString ());
  }

  @Nonnull
  public static TDEIdentifierWithLOAType createIdentifierWithLOA (@Nonnull @Nonempty final String sValue)
  {
    return createIdentifierWithLOA (sValue, null);
  }

  @Nonnull
  public static TDEIdentifierWithLOAType createIdentifierWithLOA (@Nonnull @Nonempty final String sValue,
                                                                  @Nullable final EToopLevelOfAssurance eLOA)
  {
    final TDEIdentifierWithLOAType ret = new TDEIdentifierWithLOAType ();
    ret.setValue (sValue);
    ret.setLevelOfAssurance (_getLOAValue (eLOA));
    return ret;
  }

  @Nonnull
  public static IndicatorType createIndicator (@Nonnull @Nonempty final boolean bValue)
  {
    final IndicatorType ret = new IndicatorType ();
    ret.setValue (bValue);
    return ret;
  }

  @Nonnull
  public static TextType createText (@Nonnull @Nonempty final String sValue)
  {
    final TextType ret = new TextType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static TextType createText (@Nullable final Locale aLanguage, @Nonnull @Nonempty final String sValue)
  {
    final TextType ret = new TextType ();
    if (aLanguage != null)
      ret.setLanguageID (aLanguage.getLanguage ());
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static TDETextWithLOAType createTextWithLOA (@Nonnull @Nonempty final String sValue)
  {
    return createTextWithLOA (sValue, null);
  }

  @Nonnull
  public static TDETextWithLOAType createTextWithLOA (@Nonnull @Nonempty final String sValue,
                                                      @Nullable final EToopLevelOfAssurance eLOA)
  {
    final TDETextWithLOAType ret = new TDETextWithLOAType ();
    ret.setValue (sValue);
    ret.setLevelOfAssurance (_getLOAValue (eLOA));
    return ret;
  }

  @Nonnull
  public static CodeType createCode (@Nonnull @Nonempty final String sValue)
  {
    final CodeType ret = new CodeType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static CodeType createCode (@Nonnull @Nonempty final String sSchemeID, @Nonnull @Nonempty final String sValue)
  {
    final CodeType ret = new CodeType ();
    ret.setName (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static TDECodeWithLOAType createCodeWithLOA (@Nonnull @Nonempty final String sValue)
  {
    return createCodeWithLOA (sValue, null);
  }

  @Nonnull
  public static TDECodeWithLOAType createCodeWithLOA (@Nonnull @Nonempty final String sValue,
                                                      @Nullable final EToopLevelOfAssurance eLOA)
  {
    final TDECodeWithLOAType ret = new TDECodeWithLOAType ();
    ret.setValue (sValue);
    ret.setLevelOfAssurance (_getLOAValue (eLOA));
    return ret;
  }

  @Nonnull
  public static NumericType createNumeric (@Nonnull final BigDecimal aValue)
  {
    final NumericType ret = new NumericType ();
    ret.setValue (aValue);
    return ret;
  }

  @Nonnull
  public static TDEDateWithLOAType createDateWithLOA (@Nonnull final XMLGregorianCalendar aCal)
  {
    return createDateWithLOA (aCal, null);
  }

  @Nonnull
  public static TDEDateWithLOAType createDateWithLOA (@Nonnull final XMLGregorianCalendar aCal,
                                                      @Nullable final EToopLevelOfAssurance eLOA)
  {
    final TDEDateWithLOAType ret = new TDEDateWithLOAType ();
    ret.setValue (aCal);
    ret.setLevelOfAssurance (_getLOAValue (eLOA));
    return ret;
  }

  @Nonnull
  public static TDEDateWithLOAType createDateWithLOANow ()
  {
    return createDateWithLOA (PDTXMLConverter.getXMLCalendarDateNow (), null);
  }

  @Nonnull
  public static TDEDateWithLOAType createDateWithLOANow (@Nullable final EToopLevelOfAssurance eLOA)
  {
    return createDateWithLOA (PDTXMLConverter.getXMLCalendarDateNow (), eLOA);
  }

  @Nonnull
  public static TDEPreferredDocumentMimeTypeCodeType createMimeTypeCode (@Nonnull final IMimeType aValue)
  {
    final TDEPreferredDocumentMimeTypeCodeType ret = new TDEPreferredDocumentMimeTypeCodeType ();
    ret.setValue (aValue.getAsString ());
    return ret;
  }
}
