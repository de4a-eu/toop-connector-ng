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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;

/**
 * Test class for class {@link EToopConcept_0_10_7}.
 *
 * @author Philip Helger
 */
public final class EToopConcept_0_10_7Test
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EToopConcept_0_10_7Test.class);

  @Test
  public void testBasic ()
  {
    for (final EToopConcept_0_10_7 e : EToopConcept_0_10_7.values ())
    {
      assertTrue (StringHelper.hasText (e.getID ()));
      assertSame (e, EToopConcept_0_10_7.getFromIDOrNull (e.getID ()));
    }
  }

  @Test
  public void testOldNames ()
  {
    // Taken from
    // https://docs.google.com/spreadsheets/d/1E5vPDWnfcA5cCGxzQL3tB5QZy7V65PuXoibnIN8Fop8/edit#gid=27052854
    // column c
    final String sOldNames = "CompanyCode\r\n" +
                             "companyName\r\n" +
                             "companyType\r\n" +
                             "foundationDate\r\n" +
                             "RegistrationAuthority\r\n" +
                             "registrationDate\r\n" +
                             "RegistrationNumber\r\n" +
                             "SSNumber\r\n" +
                             "VATNumber\r\n" +
                             "capitalType\r\n" +
                             "legalStatus\r\n" +
                             "legalStatusEffectiveDate\r\n" +
                             "activityDescription\r\n" +
                             "naceCode\r\n" +
                             "countryName\r\n" +
                             "locality\r\n" +
                             "postalCode\r\n" +
                             "region\r\n" +
                             "streetAddress\r\n" +
                             "EmailAddress\r\n" +
                             "FaxNumber\r\n" +
                             "TelephoneNumber\r\n" +
                             "\r\n" +
                             "birthDate\r\n" +
                             "familyName\r\n" +
                             "givenName";
    for (final String s : RegExHelper.getSplitToArray (sOldNames, "\r\n"))
      if (StringHelper.hasText (s))
        assertNotNull (EToopConcept_0_10_7.getFromIDOrNull (s));
  }

  @Test
  public void testCreateMapping ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final EToopConcept_0_10_7 e : EToopConcept_0_10_7.values ())
      if (!e.getID ().equals (e.getNewConcept ().getID ()))
        aSB.append (e.getID ()).append (" -> ").append (e.getNewConcept ().getID ()).append ('\n');
    LOGGER.info (aSB.toString ());
  }
}
