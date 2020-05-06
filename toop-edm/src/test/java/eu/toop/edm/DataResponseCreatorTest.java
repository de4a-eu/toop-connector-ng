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

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;

import eu.toop.edm.error.EToopDataElementResponseErrorCode;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.DocumentReferencePojo;
import eu.toop.edm.model.QualifiedRelationPojo;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.dcatap.CDCatAP;
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

  @Nonnull
  public static DataResponseCreator.Builder _dataResponse ()
  {
    return DataResponseCreator.builderConcept ()
                              .requestID ("c4369c4d-740e-4b64-80f0-7b209a66d629")
                              .responseStatus (ERegRepResponseStatus.SUCCESS)
                              .issueDateTime (PDTFactory.createLocalDateTime (2020, Month.FEBRUARY, 14, 19, 20, 30))
                              .dataProvider (AgentPojo.builder ().id ("12345678").idSchemeID ("VAT").name ("DPName"));
  }

  @Test
  public void testBasicResponseCreatorConcept ()
  {
    final String sConceptNS = "http://toop.eu/registered-organization";
    final QueryResponse aResponse = _dataResponse ().concept (ConceptPojo.builder ()
                                                                         .id ("ConceptID-1")
                                                                         .name (sConceptNS, "CompanyData")
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-2")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-2")
                                                                                               .valueAmount (BigDecimal.valueOf (1000001),
                                                                                                             "EUR"))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-3")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-3")
                                                                                               .valueText ("ConceptID-3 Value"))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-4")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-4")
                                                                                               .valueCode ("code"))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-5")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-5")
                                                                                               .valueIndicator (true))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-6")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-6")
                                                                                               .valuePeriod (PDTFactory.createLocalDate (2020,
                                                                                                                                         Month.JANUARY,
                                                                                                                                         2),
                                                                                                             null,
                                                                                                             PDTFactory.createLocalDate (2022,
                                                                                                                                         Month.MAY,
                                                                                                                                         5),
                                                                                                             null))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-7")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-7")
                                                                                               .valueDate (PDTFactory.createLocalDate (2020,
                                                                                                                                       Month.MAY,
                                                                                                                                       5)))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-8")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-8")
                                                                                               .valueNumeric (55))
                                                                         .addChild (ConceptPojo.builder ()
                                                                                               .id ("ConceptID-9")
                                                                                               .name (sConceptNS,
                                                                                                      "Concept-Name-9")
                                                                                               .valueErrorCode (EToopDataElementResponseErrorCode.DP_ELE_001)))
                                                    .build ();
    assertNotNull (aResponse);

    final String sXML = RegRep4Writer.queryResponse (CCAGV.XSDS).setFormattedOutput (true).getAsString (aResponse);
    assertNotNull (sXML);

    LOGGER.info (sXML);
  }

  @Test
  public void testBasicResponseCreatorDocument ()
  {
    final QueryResponse aResponse = _dataResponse ().dataset (DatasetPojo.builder ()
                                                                         .description ("bla desc")
                                                                         .title ("bla title")
                                                                         .distribution (DocumentReferencePojo.builder ()
                                                                                                             .documentURI ("URI")
                                                                                                             .documentDescription ("DocumentDescription")
                                                                                                             .documentType ("docType")
                                                                                                             .localeCode ("GR"))
                                                                         .creator (AgentPojo.builder ()
                                                                                            .name ("Agent name")
                                                                                            .address (AddressPojo.builder ()
                                                                                                                 .town ("Kewlkidshome")))
                                                                         .ids ("RE238918378", "DOC-555")
                                                                         .issuedNow ()
                                                                         .language ("en")
                                                                         .lastModifiedNow ()
                                                                         .validFrom (PDTFactory.getCurrentLocalDate ()
                                                                                               .minusMonths (1))
                                                                         .validTo (PDTFactory.getCurrentLocalDate ()
                                                                                             .plusYears (1))
                                                                         .qualifiedRelation (QualifiedRelationPojo.builder ()
                                                                                                                  .description ("LegalResourceDesc")
                                                                                                                  .title ("Name")
                                                                                                                  .id ("RE238918378")))
                                                    .build ();
    assertNotNull (aResponse);

    final String sXML = RegRep4Writer.queryResponse (CDCatAP.XSDS).setFormattedOutput (true).getAsString (aResponse);
    assertNotNull (sXML);

    if (false)
      LOGGER.info (sXML);
  }
}
