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
package eu.toop.edm.xml.cv;

import java.time.LocalDate;

import javax.annotation.Nonnull;

import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.w3.cv.ac.CvidentifierType;
import eu.toop.edm.jaxb.w3.cv.bc.BirthDateType;
import eu.toop.edm.jaxb.w3.cv.bc.BirthNameType;
import eu.toop.edm.jaxb.w3.cv.bc.GenderCodeType;
import eu.toop.edm.jaxb.w3.cv.bc.GivenNameType;
import eu.toop.edm.jaxb.w3.cv.bc.IdentifierType;
import eu.toop.edm.jaxb.w3.cv.person.CvpersonType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.FamilyNameType;

public class PersonPojo
{
  private final String m_sFamilyName;
  private final String m_sGivenName;
  private final String m_sGenderCode;
  private final String m_sBirthName;
  private final LocalDate m_aBirthDate;
  private final String m_sID;

  public PersonPojo (final String sFamilyName,
                     final String sGivenName,
                     final String sGenderCode,
                     final String sBirthName,
                     final LocalDate aBirthDate,
                     final String sID)
  {
    m_sFamilyName = sFamilyName;
    m_sGivenName = sGivenName;
    m_sGenderCode = sGenderCode;
    m_sBirthName = sBirthName;
    m_aBirthDate = aBirthDate;
    m_sID = sID;
  }

  @Nonnull
  public CvpersonType getAsPerson ()
  {
    final CvpersonType ret = new CvpersonType ();

    if (StringHelper.hasText (m_sFamilyName))
    {
      final FamilyNameType aFamilyName = new FamilyNameType ();
      aFamilyName.setValue (m_sFamilyName);
      ret.addFamilyName (aFamilyName);
    }
    if (StringHelper.hasText (m_sGivenName))
    {
      final GivenNameType aGivenName = new GivenNameType ();
      aGivenName.setValue (m_sGivenName);
      ret.addGivenName (aGivenName);
    }
    if (StringHelper.hasText (m_sGenderCode))
    {
      final GenderCodeType aGC = new GenderCodeType ();
      aGC.setValue (m_sGenderCode);
      ret.addGenderCode (aGC);
    }
    if (StringHelper.hasText (m_sBirthName))
    {
      final BirthNameType aBirthName = new BirthNameType ();
      aBirthName.setValue (m_sBirthName);
      ret.addBirthName (aBirthName);
    }
    if (m_aBirthDate != null)
    {
      final BirthDateType aBirthDate = new BirthDateType ();
      aBirthDate.setValue (PDTXMLConverter.getXMLCalendarDate (m_aBirthDate));
      ret.addBirthDate (aBirthDate);
    }
    if (StringHelper.hasText (m_sID))
    {
      final CvidentifierType aID = new CvidentifierType ();
      final IdentifierType aIdentifier = new IdentifierType ();
      aIdentifier.setValue (m_sID);
      aID.setIdentifier (aIdentifier);
      ret.addCvidentifier (aID);
    }

    return ret;
  }

  @Nonnull
  public static PersonPojo createMinimum ()
  {
    return new PersonPojo (null, null, null, null, null, null);
  }
}
