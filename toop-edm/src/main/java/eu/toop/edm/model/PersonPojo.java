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

import java.time.LocalDate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.w3.cv.ac.CoreLocationType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonBirthDateType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonBirthNameType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonFamilyNameType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonGenderCodeType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonGivenNameType;
import eu.toop.edm.jaxb.w3.cv.bc.PersonIDType;

/**
 * Representation of a "Person"
 *
 * @author Philip Helger
 */
public class PersonPojo
{
  private final String m_sID;
  private final String m_sIDSchemeID;
  private final String m_sFamilyName;
  private final String m_sGivenName;
  private final String m_sGenderCode;
  private final String m_sBirthName;
  private final LocalDate m_aBirthDate;
  private final String m_sBirthTown;
  private final AddressPojo m_aAddress;

  public PersonPojo (@Nonnull final String sID,
                     @Nonnull final String sIDSchemeID,
                     @Nonnull final String sFamilyName,
                     @Nonnull final String sGivenName,
                     @Nullable final String sGenderCode,
                     @Nullable final String sBirthName,
                     @Nonnull final LocalDate aBirthDate,
                     @Nullable final String sBirthTown,
                     @Nullable final AddressPojo aAddress)
  {
    ValueEnforcer.notNull (sID, "ID");
    ValueEnforcer.notNull (sIDSchemeID, "IDSchemeID");
    ValueEnforcer.notNull (sFamilyName, "FamilyName");
    ValueEnforcer.notNull (sGivenName, "GivenName");
    ValueEnforcer.notNull (aBirthDate, "BirthDate");

    m_sID = sID;
    m_sIDSchemeID = sIDSchemeID;
    m_sFamilyName = sFamilyName;
    m_sGivenName = sGivenName;
    m_sGenderCode = sGenderCode;
    m_sBirthName = sBirthName;
    m_aBirthDate = aBirthDate;
    m_sBirthTown = sBirthTown;
    m_aAddress = aAddress;
  }

  @Nonnull
  public final String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public final String getIDSchemeID ()
  {
    return m_sIDSchemeID;
  }

  @Nonnull
  public final String getFamilyName ()
  {
    return m_sFamilyName;
  }

  @Nonnull
  public final String getGivenName ()
  {
    return m_sGivenName;
  }

  @Nullable
  public final String getGenderCode ()
  {
    return m_sGenderCode;
  }

  @Nullable
  public final String getBirthName ()
  {
    return m_sBirthName;
  }

  @Nonnull
  public final LocalDate getBirthDate ()
  {
    return m_aBirthDate;
  }

  @Nullable
  public final String getBirthTown ()
  {
    return m_sBirthTown;
  }

  @Nullable
  public final AddressPojo getAddress ()
  {
    return m_aAddress;
  }

  @Nonnull
  public CorePersonType getAsCorePerson ()
  {
    final CorePersonType ret = new CorePersonType ();

    if (StringHelper.hasText (m_sID))
    {
      final PersonIDType aID = new PersonIDType ();
      aID.setValue (m_sID);
      aID.setSchemeID (m_sIDSchemeID);
      ret.addPersonID (aID);
    }
    if (StringHelper.hasText (m_sFamilyName))
    {
      final PersonFamilyNameType aFamilyName = new PersonFamilyNameType ();
      aFamilyName.setValue (m_sFamilyName);
      ret.addPersonFamilyName (aFamilyName);
    }
    if (StringHelper.hasText (m_sGivenName))
    {
      final PersonGivenNameType aGivenName = new PersonGivenNameType ();
      aGivenName.setValue (m_sGivenName);
      ret.addPersonGivenName (aGivenName);
    }
    if (StringHelper.hasText (m_sGenderCode))
    {
      final PersonGenderCodeType aGC = new PersonGenderCodeType ();
      aGC.setValue (m_sGenderCode);
      ret.addPersonGenderCode (aGC);
    }
    if (StringHelper.hasText (m_sBirthName))
    {
      final PersonBirthNameType aBirthName = new PersonBirthNameType ();
      aBirthName.setValue (m_sBirthName);
      ret.addPersonBirthName (aBirthName);
    }
    if (m_aBirthDate != null)
    {
      final PersonBirthDateType aBirthDate = new PersonBirthDateType ();
      aBirthDate.setValue (PDTXMLConverter.getXMLCalendarDate (m_aBirthDate));
      ret.addPersonBirthDate (aBirthDate);
    }
    if (StringHelper.hasText (m_sBirthTown))
    {
      final CoreLocationType aLocation = new CoreLocationType ();
      aLocation.addLocationCoreAddress (AddressPojo.builder ().town (m_sBirthTown).build ().getAsCoreAddress ());
      ret.addPersonPlaceOfBirthCoreLocation (aLocation);
    }
    if (m_aAddress != null)
      ret.addPersonCoreAddress (m_aAddress.getAsCoreAddress ());

    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PersonPojo rhs = (PersonPojo) o;
    return EqualsHelper.equals (m_sID, rhs.m_sID) &&
           EqualsHelper.equals (m_sIDSchemeID, rhs.m_sIDSchemeID) &&
           EqualsHelper.equals (m_sFamilyName, rhs.m_sFamilyName) &&
           EqualsHelper.equals (m_sGivenName, rhs.m_sGivenName) &&
           EqualsHelper.equals (m_sGenderCode, rhs.m_sGenderCode) &&
           EqualsHelper.equals (m_sBirthName, rhs.m_sBirthName) &&
           EqualsHelper.equals (m_aBirthDate, rhs.m_aBirthDate) &&
           EqualsHelper.equals (m_sBirthTown, rhs.m_sBirthTown) &&
           EqualsHelper.equals (m_aAddress, rhs.m_aAddress);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID)
                                       .append (m_sIDSchemeID)
                                       .append (m_sFamilyName)
                                       .append (m_sGivenName)
                                       .append (m_sGenderCode)
                                       .append (m_sBirthName)
                                       .append (m_aBirthDate)
                                       .append (m_sBirthTown)
                                       .append (m_aAddress)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("IDSchemeID", m_sIDSchemeID)
                                       .append ("FamilyName", m_sFamilyName)
                                       .append ("GivenName", m_sGivenName)
                                       .append ("GenderCode", m_sGenderCode)
                                       .append ("BirthName", m_sBirthName)
                                       .append ("BirthDate", m_aBirthDate)
                                       .append ("BirthTown", m_sBirthTown)
                                       .append ("Address", m_aAddress)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final CorePersonType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      if (a.hasPersonIDEntries ())
        ret.id (a.getPersonIDAtIndex (0).getValue ()).idSchemeID (a.getPersonIDAtIndex (0).getSchemeID ());
      if (a.hasPersonFamilyNameEntries ())
        ret.familyName (a.getPersonFamilyNameAtIndex (0).getValue ());
      if (a.hasPersonGivenNameEntries ())
        ret.givenName (a.getPersonGivenNameAtIndex (0).getValue ());
      if (a.hasPersonGenderCodeEntries ())
        ret.genderCode (a.getPersonGenderCodeAtIndex (0).getValue ());
      if (a.hasPersonBirthNameEntries ())
        ret.birthName (a.getPersonBirthNameAtIndex (0).getValue ());
      if (a.hasPersonBirthDateEntries ())
        ret.birthDate (a.getPersonBirthDateAtIndex (0).getValue ());
      if (a.hasPersonPlaceOfBirthCoreLocationEntries ())
      {
        final CoreLocationType aLoc = a.getPersonPlaceOfBirthCoreLocationAtIndex (0);
        if (aLoc.hasLocationCoreAddressEntries ())
          ret.birthTown (AddressPojo.builder (aLoc.getLocationCoreAddressAtIndex (0)).build ().getTown ());
      }
      if (a.hasPersonCoreAddressEntries ())
        ret.address (AddressPojo.builder (a.getPersonCoreAddressAtIndex (0)));
    }
    return ret;
  }

  public static class Builder
  {
    private String m_sID;
    private String m_sIDSchemeID;
    private String m_sFamilyName;
    private String m_sGivenName;
    private String m_sGenderCode;
    private String m_sBirthName;
    private LocalDate m_aBirthDate;
    private String m_sBirthTown;
    private AddressPojo m_aAddress;

    public Builder ()
    {}

    @Nonnull
    public Builder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nonnull
    public Builder idSchemeID (@Nullable final String s)
    {
      m_sIDSchemeID = s;
      return this;
    }

    @Nonnull
    public Builder familyName (@Nullable final String s)
    {
      m_sFamilyName = s;
      return this;
    }

    @Nonnull
    public Builder givenName (@Nullable final String s)
    {
      m_sGivenName = s;
      return this;
    }

    @Nonnull
    public Builder genderCode (@Nullable final EGenderCode e)
    {
      return genderCode (e == null ? null : e.getID ());
    }

    @Nonnull
    public Builder genderCode (@Nullable final String s)
    {
      m_sGenderCode = s;
      return this;
    }

    @Nonnull
    public Builder birthName (@Nullable final String s)
    {
      m_sBirthName = s;
      return this;
    }

    @Nonnull
    public Builder birthDate (@Nullable final XMLGregorianCalendar a)
    {
      return birthDate (PDTXMLConverter.getLocalDate (a));
    }

    @Nonnull
    public Builder birthDate (@Nullable final LocalDate a)
    {
      m_aBirthDate = a;
      return this;
    }

    @Nonnull
    public Builder birthTown (@Nullable final String s)
    {
      m_sBirthTown = s;
      return this;
    }

    @Nonnull
    public Builder address (@Nullable final AddressPojo.Builder a)
    {
      return address (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder address (@Nullable final AddressPojo a)
    {
      m_aAddress = a;
      return this;
    }

    public void checkConsistency ()
    {
      if (StringHelper.hasNoText (m_sID))
        throw new IllegalStateException ("ID must be present");
      if (StringHelper.hasNoText (m_sIDSchemeID))
        throw new IllegalStateException ("ID SchemeID must be present");
      if (StringHelper.hasNoText (m_sFamilyName))
        throw new IllegalStateException ("Family Name must be present");
      if (StringHelper.hasNoText (m_sGivenName))
        throw new IllegalStateException ("Given Name must be present");
      if (m_aBirthDate == null)
        throw new IllegalStateException ("Birth Date must be present");
    }

    @Nonnull
    public PersonPojo build ()
    {
      checkConsistency ();

      return new PersonPojo (m_sID,
                             m_sIDSchemeID,
                             m_sFamilyName,
                             m_sGivenName,
                             m_sGenderCode,
                             m_sBirthName,
                             m_aBirthDate,
                             m_sBirthTown,
                             m_aAddress);
    }
  }
}
