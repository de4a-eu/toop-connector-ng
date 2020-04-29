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

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;

/**
 * Test class for class {@link DatasetPojo}
 *
 * @author Philip Helger
 */
public final class DatasetPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DatasetPojoTest.class);

  @Test
  public void testBasic ()
  {
    final DatasetPojo x = DatasetPojo.builder ()
                                     .title ("bla title")
                                     .description ("bla desc")
                                     .creator (AgentPojo.builder ()
                                                        .name ("Agent name")
                                                        .address (AddressPojo.builder ().town ("Kewlkidshome")))
                                     .id ("my ID")
                                     .language ("en")
                                     .issuedNow ()
                                     .lastModifiedNow ()
                                     .validFrom (PDTFactory.getCurrentLocalDate ().minusMonths (1))
                                     .validTo (PDTFactory.getCurrentLocalDate ().plusYears (1))
                                     .build ();
    final DCatAPDatasetType aAgent = x.getAsDataset ();
    assertNotNull (aAgent);

    final DatasetMarshaller m = new DatasetMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));
  }

  @Test
  public void testMinimum ()
  {
    final DatasetPojo x = DatasetPojo.builder ().title ("bla title").description ("bla desc").build ();
    assertNotNull (x);

    final DCatAPDatasetType aAgent = x.getAsDataset ();
    assertNotNull (aAgent);

    final DatasetMarshaller m = new DatasetMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aAgent));
    LOGGER.info (m.getAsString (aAgent));
  }
}
