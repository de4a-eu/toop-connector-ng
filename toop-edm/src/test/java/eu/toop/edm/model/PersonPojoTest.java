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

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import eu.toop.edm.xml.cv.PersonMarshaller;

/**
 * Test class for class {@link PersonPojo}.
 *
 * @author Philip Helger
 */
public final class PersonPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (PersonPojoTest.class);

  private static void _validate (@Nonnull final PersonPojo x)
  {
    final CorePersonType aPerson = x.getAsCorePerson ();
    assertNotNull (aPerson);

    final PersonMarshaller m = new PersonMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aPerson));
    LOGGER.info (m.getAsString (aPerson));
  }

  @Test
  public void testBasic ()
  {
    final AddressPojo a = new AddressPojo ("FullAddress",
                                           "StreetName",
                                           "BuildingNumber",
                                           "Town",
                                           "PostalCode",
                                           "CountryCode");
    final PersonPojo x = new PersonPojo ("ID",
                                         "idSchemeID",
                                         "FamilyName",
                                         "GivenName",
                                         "GenderCode",
                                         "BirthName",
                                         PDTFactory.getCurrentLocalDate (),
                                         a);
    _validate (x);
  }

  @Test
  public void testMinimum ()
  {
    final PersonPojo x = PersonPojo.createMinimum ();
    _validate (x);
  }
}
