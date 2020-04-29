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
package eu.toop.edm;

import static org.junit.Assert.assertNotNull;

import java.time.Month;
import java.util.Locale;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.EGenderCode;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;

/**
 * Test class for class {@link DataRequestCreator}
 *
 * @author Philip Helger
 */
public final class DataRequestCreatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DataRequestCreatorTest.class);

  @Test
  public void testBasicRequestCreatorConcept ()
  {
    final QueryRequest aRequest = DataRequestCreator.builderConcept ()
                                                    .issueDateTimeNow ()
                                                    .procedure (Locale.UK, "I am the producer")
                                                    .fullfillingRequirement (null)
                                                    .consentToken ("I consent")
                                                    .datasetIdentifier ("dsID")
                                                    .dataConsumer (AgentPojo.builder ()
                                                                            .id ("DE730757727")
                                                                            .idSchemeID ("VAT")
                                                                            .name ("Company Name")
                                                                            .address (AddressPojo.builder ()
                                                                                                 .fullAddress ("Prince Street 15")
                                                                                                 .streetName ("Prince Street")
                                                                                                 .buildingNumber ("15")
                                                                                                 .town ("Liverpool")
                                                                                                 .postalCode ("15115")
                                                                                                 .countryCode ("GB")))
                                                    .dataSubject (PersonPojo.builder ()
                                                                            .familyName ("DataSubjectFamily")
                                                                            .genderCode (EGenderCode.F)
                                                                            .birthDate (PDTFactory.createLocalDate (2000,
                                                                                                                    Month.FEBRUARY,
                                                                                                                    28)))
                                                    .authorizedRepresentative (PersonPojo.builder ()
                                                                                         .givenName ("Authhorizer"))
                                                    .concept (ConceptPojo.builder ()
                                                                         .randomID ()
                                                                         .name ("http://toop.eu/registered-organization",
                                                                                "CompanyName"))
                                                    .build ();
    assertNotNull (aRequest);

    final String sXML = RegRep4Writer.queryRequest (CCAGV.XSDS).setFormattedOutput (true).getAsString (aRequest);
    assertNotNull (sXML);

    LOGGER.info (sXML);
  }
}
