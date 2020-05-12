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

import com.helger.commons.mock.CommonsTestHelper;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.xml.cagv.AgentMarshaller;

/**
 * Test class for class {@link AgentPojo}
 *
 * @author Philip Helger
 */
public final class AgentPojoTest
{
  private static void _testWriteAndRead (@Nonnull final AgentPojo x)
  {
    assertNotNull (x);

    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));

    final AgentPojo y = AgentPojo.builder (aAgent).build ();
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testBasic ()
  {
    final AgentPojo x = AgentPojo.builder ()
                                 .id ("ID")
                                 .idSchemeID ("IDType")
                                 .name ("Name")
                                 .address (AddressPojo.builder ()
                                                      .fullAddress ("FullAddress")
                                                      .streetName ("StreetName")
                                                      .buildingNumber ("BuildingNumber")
                                                      .town ("Town")
                                                      .postalCode ("PostalCode")
                                                      .countryCode ("CountryCode"))
                                 .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testMinimum ()
  {
    final AgentPojo x = AgentPojo.builder ().build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testNoAddress ()
  {
    final AgentPojo x = AgentPojo.builder ().id ("ID").idSchemeID ("IDType").name ("Name").build ();
    _testWriteAndRead (x);
  }
}
