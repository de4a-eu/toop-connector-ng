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

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.jaxb.w3.cv.business.CvbusinessType;

/**
 * Test class for class {@link BusinessPojo}.
 *
 * @author Philip Helger
 */
public final class BusinessPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (BusinessPojoTest.class);

  private static void _validate (@Nonnull final BusinessPojo x)
  {
    final CvbusinessType aBusiness = x.getAsBusiness ();
    assertNotNull (aBusiness);

    final BusinessMarshaller m = new BusinessMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aBusiness));
    LOGGER.info (m.getAsString (aBusiness));
  }

  @Test
  public void testBasic ()
  {
    final BusinessPojo x = new BusinessPojo ("LegalName",
                                             "LegalID",
                                             "LegalIDType",
                                             "ID",
                                             "IDType",
                                             "FullAddress",
                                             "StreetName",
                                             "BuildingNumber",
                                             "Town",
                                             "PostalCode",
                                             "CountryCode");
    _validate (x);
  }

  @Test
  public void testMinimum ()
  {
    final BusinessPojo x = BusinessPojo.createMinimum ();
    _validate (x);
  }
}
