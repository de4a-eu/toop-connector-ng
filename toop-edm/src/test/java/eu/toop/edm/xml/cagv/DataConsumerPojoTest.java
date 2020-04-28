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
package eu.toop.edm.xml.cagv;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.edm.jaxb.cv.agent.AgentType;

/**
 * Test class for class {@link DataConsumerPojo}
 *
 * @author Philip Helger
 */
public final class DataConsumerPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DataConsumerPojoTest.class);

  @Test
  public void testBasic ()
  {
    final DataConsumerPojo x = new DataConsumerPojo ("ID",
                                                     "IDType",
                                                     "Name",
                                                     "FullAddress",
                                                     "StreetName",
                                                     "BuildingNumber",
                                                     "Town",
                                                     "PostalCode",
                                                     "CountryCode");
    final AgentType aAgent = x.getAsAgent ();
    assertNotNull (aAgent);

    final AgentMarshaller m = new AgentMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));
  }
}
