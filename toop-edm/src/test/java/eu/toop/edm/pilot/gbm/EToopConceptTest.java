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
package eu.toop.edm.pilot.gbm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;

/**
 * Test class for class {@link EToopConcept}.
 *
 * @author Philip Helger
 */
public final class EToopConceptTest
{
  @Test
  public void testBasic ()
  {
    for (final EToopConcept e : EToopConcept.values ())
    {
      assertTrue (StringHelper.hasText (e.getID ()));
      assertSame (e, EToopConcept.getFromIDOrNull (e.getID ()));
    }
  }

  @Test
  public void testOldNames ()
  {
    // Taken from
    // https://docs.google.com/spreadsheets/d/1E5vPDWnfcA5cCGxzQL3tB5QZy7V65PuXoibnIN8Fop8/edit#gid=27052854
    // column d
    final String sOldNames = "CompanyCode\r\n" +
                             "CompanyName\r\n" +
                             "CompanyType\r\n" +
                             "FoundationDate\r\n" +
                             "RegistrationAuthority\r\n" +
                             "RegistrationDate\r\n" +
                             "RegistrationNumber\r\n" +
                             "SSNumber\r\n" +
                             "VATNumber\r\n" +
                             "CapitalType\r\n" +
                             "LegalStatus\r\n" +
                             "LegalStatusEffectiveDate\r\n" +
                             "ActivityDescription\r\n" +
                             "NaceCode\r\n" +
                             "CountryName\r\n" +
                             "Locality\r\n" +
                             "PostalCode\r\n" +
                             "Region\r\n" +
                             "StreetAddress\r\n" +
                             "EmailAddress\r\n" +
                             "FaxNumber\r\n" +
                             "TelephoneNumber\r\n" +
                             "\r\n" +
                             "LegalRepresentativeBirthDate\r\n" +
                             "LegalRepresentativeFamilyName\r\n" +
                             "LegalRepresentativeGivenName";
    for (final String s : RegExHelper.getSplitToArray (sOldNames, "\r\n"))
      if (StringHelper.hasText (s))
        assertNotNull (EToopConcept.getFromIDOrNull (s));
  }
}
