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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private static final Logger LOGGER = LoggerFactory.getLogger (AgentPojoTest.class);

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
    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));

    final AgentPojo y = AgentPojo.builder (aAgent).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testMinimum ()
  {
    final AgentPojo x = AgentPojo.builder ().build ();
    assertNotNull (x);

    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));

    final AgentPojo y = AgentPojo.builder (aAgent).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testNoAddress ()
  {
    final AgentPojo x = AgentPojo.builder ().id ("ID").idSchemeID ("IDType").name ("Name").build ();
    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));

    final AgentPojo y = AgentPojo.builder (aAgent).build ();
    CommonsTestHelper.testEqualsImplementationWithEqualContentObject (x, y);
  }
}
