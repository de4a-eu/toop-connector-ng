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
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Month;

import javax.annotation.Nonnull;

import eu.toop.edm.creator.EDMResponseCreator;
import eu.toop.edm.model.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.error.EToopDataElementResponseErrorCode;
import eu.toop.edm.schematron.SchematronEDM2Validator;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.dcatap.CDCatAP;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryResponse;

/**
 * Test class for class {@link EDMResponseCreator}
 *
 * @author Philip Helger
 */
public final class EDMResponseCreatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EDMResponseCreatorTest.class);

  @Nonnull
  public static EDMResponse.Builder _dataResponseConcept ()
  {
    return EDMResponse.builderConcept ()
                             .requestID ("c4369c4d-740e-4b64-80f0-7b209a66d629")
                             .responseStatus (ERegRepResponseStatus.SUCCESS)
                             .issueDateTime (PDTFactory.createLocalDateTime (2020, Month.FEBRUARY, 14, 19, 20, 30))
                             .dataProvider (AgentPojo.builder ().id ("12345678").idSchemeID ("VAT").name ("DPName"));
  }

  @Nonnull
  public static EDMResponse.Builder _dataResponseDocument ()
  {
    return EDMResponse.builderDocument ()
                             .requestID ("c4369c4d-740e-4b64-80f0-7b209a66d629")
                             .responseStatus (ERegRepResponseStatus.SUCCESS)
                             .issueDateTime (PDTFactory.createLocalDateTime (2020, Month.FEBRUARY, 14, 19, 20, 30))
                             .dataProvider (AgentPojo.builder ().id ("12345678").idSchemeID ("VAT").name ("DPName"));
  }

  @Test
  public void testBasicResponseCreatorConcept ()
  {
    final String sConceptNS = "http://toop.eu/registered-organization";
    final QueryResponse aResponse = _dataResponseConcept ().concept (ConceptPojo.builder ()
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
                                                                                                      .valueNumeric (23456.34))
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
                                                           .build ().getAsQueryResponse();
    assertNotNull (aResponse);

    final RegRep4Writer <QueryResponse> aWriter = RegRep4Writer.queryResponse (CCAGV.XSDS).setFormattedOutput (true);
    final String sXML = aWriter.getAsString (aResponse);
    assertNotNull (sXML);

    LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aWriter.getAsDocument (aResponse);
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Test
  public void testBasicResponseCreatorDocument ()
  {
    final QueryResponse aResponse = _dataResponseDocument ().dataset (DatasetPojo.builder ()
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
                                                            .build ().getAsQueryResponse();
    assertNotNull (aResponse);

    final RegRep4Writer <QueryResponse> aWriter = RegRep4Writer.queryResponse (CDCatAP.XSDS).setFormattedOutput (true);
    final String sXML = aWriter.getAsString (aResponse);
    assertNotNull (sXML);

    if (false)
      LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aWriter.getAsDocument (aResponse);
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }
}
