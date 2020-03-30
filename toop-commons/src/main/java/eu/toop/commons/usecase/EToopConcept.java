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
package eu.toop.commons.usecase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;

/**
 * Predefined TOOP concepts for "registered organization". Source
 * http://wiki.ds.unipi.gr/display/TOOPPILOTS/Datasets <br>
 * Heavily changed from 0.10.7 to 0.10.8
 *
 * @author Philip Helger
 * @since 0.10.0
 * @since 0.10.8
 * @see EToopConcept_0_10_7
 */
public enum EToopConcept implements IToopConcept
{
  ACTIVITY_DESCRIPTION ("ActivityDescription"),
  BIRTH_DATE ("LegalRepresentativeBirthDate"),
  CAPTIAL_TYPE ("CapitalType"),
  COMPANY_CODE ("CompanyCode"),
  COMPANY_NAME ("CompanyName"),
  COMPANY_TYPE ("CompanyType"),
  COUNTRY_NAME ("CountryName"),
  EMAIL_ADDRESS ("EmailAddress"),
  FAMILY_NAME ("LegalRepresentativeFamilyName"),
  FAX_NUMBER ("FaxNumber"),
  FOUNDATION_DATE ("FoundationDate"),
  GIVEN_NAME ("LegalRepresentativeGivenName"),
  HAS_LEGAL_REPRESENTATIVE ("HasLegalRepresentative"),
  LEGAL_STATUS ("LegalStatus"),
  LEGAL_STATUS_EFFECTIVE_DATE ("LegalStatusEffectiveDate"),
  LOCALITY ("Locality"),
  NACE_CODE ("NaceCode"),
  PERSON ("Person"),
  POSTAL_CODE ("PostalCode"),
  REGION ("Region"),
  REGISTERED_ORGANIZATION ("RegisteredOrganization"),
  REGISTRATION_AUTH ("RegistrationAuthority"),
  REGISTRATION_DATE ("RegistrationDate"),
  REGISTRATION_NUMBER ("RegistrationNumber"),
  SOCIAL_SEC_NUMBER ("SSNumber"),
  STREET_ADDRESS ("StreetAddress"),
  TELEPHONE_NUMBER ("TelephoneNumber"),
  VAT_NUMBER ("VATNumber");

  private final String m_sID;

  EToopConcept (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getConceptNamespaceURI ()
  {
    return "http://toop.eu/registered-organization";
  }

  @Nullable
  public static EToopConcept getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopConcept.class, sID);
  }
}
