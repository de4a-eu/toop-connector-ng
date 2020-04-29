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

import java.math.BigDecimal;
import java.time.Month;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEVValueHelper;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryResponse;

/**
 * Test class for class {@link DataResponseCreator}
 *
 * @author Philip Helger
 */
public final class DataResponseCreatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DataResponseCreatorTest.class);

  @Test
  public void testBasicRequestCreatorConcept ()
  {
    final String sConceptNS = "http://toop.eu/registered-organization";
    final QueryResponse aResponse = DataResponseCreator.builderConcept ()
                                                       .requestID ("c4369c4d-740e-4b64-80f0-7b209a66d629")
                                                       .responseStatus (ERegRepResponseStatus.SUCCESS)
                                                       .issueDateTime (PDTFactory.createLocalDateTime (2020,
                                                                                                       Month.FEBRUARY,
                                                                                                       14,
                                                                                                       19,
                                                                                                       20,
                                                                                                       30))
                                                       .dataProvider (AgentPojo.builder ()
                                                                               .id ("12345678")
                                                                               .idSchemeID ("VAT")
                                                                               .name ("DPName"))
                                                       .concept (ConceptPojo.builder ()
                                                                            .id ("ConceptID-1")
                                                                            .name (sConceptNS, "CompanyData")
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-2")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-2")
                                                                                                  .value (CCCEVValueHelper.createAmount (BigDecimal.valueOf (1000001),
                                                                                                                                         "EUR")))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-3")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-3")
                                                                                                  .value (CCCEVValueHelper.createText ("ConceptID-3 Value")))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-4")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-4")
                                                                                                  .value (CCCEVValueHelper.createCode ("code")))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-5")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-5")
                                                                                                  .value (CCCEVValueHelper.createIndicator (true)))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-6")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-6")
                                                                                                  .value (CCCEVValueHelper.createPeriod (PDTFactory.createLocalDate (2020,
                                                                                                                                                                     Month.JANUARY,
                                                                                                                                                                     2),
                                                                                                                                         null,
                                                                                                                                         PDTFactory.createLocalDate (2022,
                                                                                                                                                                     Month.MAY,
                                                                                                                                                                     5),
                                                                                                                                         null)))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-7")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-7")
                                                                                                  .value (CCCEVValueHelper.createDate (PDTFactory.createLocalDate (2020,
                                                                                                                                                                   Month.MAY,
                                                                                                                                                                   5))))
                                                                            .addChild (ConceptPojo.builder ()
                                                                                                  .id ("ConceptID-8")
                                                                                                  .name (sConceptNS,
                                                                                                         "Concept-Name-8")
                                                                                                  .value (CCCEVValueHelper.createNumeric (55))))
                                                       .build ();
    assertNotNull (aResponse);

    final String sXML = RegRep4Writer.queryResponse (CCAGV.XSDS).setFormattedOutput (true).getAsString (aResponse);
    assertNotNull (sXML);

    LOGGER.info (sXML);
  }
}
