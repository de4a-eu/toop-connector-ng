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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.time.Month;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;

import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.DocumentReferencePojo;
import eu.toop.edm.model.QualifiedRelationPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.regrep.ERegRepResponseStatus;

public final class EDMResponseTest
{

  @Test
  public void createConceptResponse ()
  {
    final EDMResponse res = new EDMResponse.Builder ().queryDefinition (EQueryDefinitionType.CONCEPT)
                                                      .requestID (UUID.randomUUID ())
                                                      .issueDateTimeNow ()
                                                      .concept (ConceptPojo.builder ()
                                                                           .id ("ConceptID-1")
                                                                           .name (EToopConcept.REGISTERED_ORGANIZATION)
                                                                           .addChild (ConceptPojo.builder ()
                                                                                                 .name (EToopConcept.COMPANY_NAME)
                                                                                                 .valueText ("Helger Enterprises"))
                                                                           .addChild (ConceptPojo.builder ()
                                                                                                 .name (EToopConcept.FAX_NUMBER)
                                                                                                 .valueText ("342342424"))
                                                                           .addChild (ConceptPojo.builder ()
                                                                                                 .name (EToopConcept.FOUNDATION_DATE)
                                                                                                 .valueDate (PDTFactory.createLocalDate (1960,
                                                                                                                                         Month.AUGUST,
                                                                                                                                         12)))
                                                                           .build ())
                                                      .dataProvider (AgentPojo.builder ()
                                                                              .address (AddressPojo.builder ()
                                                                                                   .town ("MyTown")
                                                                                                   .streetName ("MyStreet")
                                                                                                   .buildingNumber ("22")
                                                                                                   .countryCode ("GR")
                                                                                                   .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                   .postalCode ("11134")
                                                                                                   .build ())
                                                                              .name ("DP NAME")
                                                                              .id ("1234")
                                                                              .idSchemeID ("VAT")
                                                                              .build ())
                                                      .responseStatus (ERegRepResponseStatus.SUCCESS)
                                                      .specificationIdentifier ("Niar")
                                                      .build ();
    assertNotNull (res);
  }

  @Test
  public void createDocumentResponse ()
  {
    final EDMResponse res = new EDMResponse.Builder ().queryDefinition (EQueryDefinitionType.DOCUMENT)
                                                      .requestID (UUID.randomUUID ())
                                                      .issueDateTimeNow ()
                                                      .dataset ((DatasetPojo.builder ()
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
                                                                                                                     .id ("RE238918378"))).build ())
                                                      .dataProvider (AgentPojo.builder ()
                                                                              .address (AddressPojo.builder ()
                                                                                                   .town ("MyTown")
                                                                                                   .streetName ("MyStreet")
                                                                                                   .buildingNumber ("22")
                                                                                                   .countryCode ("GR")
                                                                                                   .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                   .postalCode ("11134")
                                                                                                   .build ())
                                                                              .name ("DP NAME")
                                                                              .id ("1234")
                                                                              .idSchemeID ("VAT")
                                                                              .build ())
                                                      .responseStatus (ERegRepResponseStatus.SUCCESS)
                                                      .specificationIdentifier ("Niar")
                                                      .build ();
  }

  // This attempts to create an EDMResponse with a dataset element but with
  // ConceptQuery set as the QueryDefinition
  // which is not permitted and fails
  @Test (expected = IllegalStateException.class)
  public void createDocumentResponseWithConceptType ()
  {
    final EDMResponse res = new EDMResponse.Builder ().queryDefinition (EQueryDefinitionType.CONCEPT)
                                                      .requestID (UUID.randomUUID ())
                                                      .issueDateTimeNow ()
                                                      .dataset ((DatasetPojo.builder ()
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
                                                                                                                     .id ("RE238918378"))).build ())
                                                      .dataProvider (AgentPojo.builder ()
                                                                              .address (AddressPojo.builder ()
                                                                                                   .town ("MyTown")
                                                                                                   .streetName ("MyStreet")
                                                                                                   .buildingNumber ("22")
                                                                                                   .countryCode ("GR")
                                                                                                   .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                   .postalCode ("11134")
                                                                                                   .build ())
                                                                              .name ("DP NAME")
                                                                              .id ("1234")
                                                                              .idSchemeID ("VAT")
                                                                              .build ())
                                                      .responseStatus (ERegRepResponseStatus.SUCCESS)
                                                      .specificationIdentifier ("Niar")
                                                      .build ();
  }

  @Test
  public void testEDMConceptResponseExport () throws JAXBException, XMLStreamException, FileNotFoundException
  {
    final EDMResponse aResp = EDMExtractors.extractEDMResponse (ClassPathResource.getInputStream ("Concept Response.xml"));
    assertNotNull (aResp);

    final byte [] aBytes = aResp.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMResponse aResp2 = EDMExtractors.extractEDMResponse (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aResp, aResp2);
  }

  @Test
  public void testEDMDocumentResponseExport () throws JAXBException, XMLStreamException, FileNotFoundException
  {
    final EDMResponse aResp = EDMExtractors.extractEDMResponse (ClassPathResource.getInputStream ("Document Response.xml"));
    assertNotNull (aResp);

    final byte [] aBytes = aResp.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMResponse aResp2 = EDMExtractors.extractEDMResponse (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aResp, aResp2);
  }
}
