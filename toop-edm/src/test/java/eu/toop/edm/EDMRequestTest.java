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
import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.mime.CMimeType;

import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.model.EDistributionFormat;
import eu.toop.edm.model.EGenderCode;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;

public final class EDMRequestTest
{
  @Test
  public void createEDMDocumentRequestNP ()
  {
    final EDMRequest request = EDMRequest.builder ()
                                         .randomID ()
                                         .queryDefinition (EQueryDefinitionType.DOCUMENT)
                                         .issueDateTimeNow ()
                                         .procedure (Locale.US, "GBM Procedure")
                                         .dataConsumer (AgentPojo.builder ()
                                                                 .address (AddressPojo.builder ()
                                                                                      .town ("MyTown")
                                                                                      .streetName ("MyStreet")
                                                                                      .buildingNumber ("22")
                                                                                      .countryCode ("GR")
                                                                                      .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                      .postalCode ("11134"))
                                                                 .name ("DC NAME")
                                                                 .id ("1234")
                                                                 .idSchemeID ("VAT"))
                                         .authorizedRepresentative (PersonPojo.builder ()
                                                                              .address (AddressPojo.builder ()
                                                                                                   .town ("MyTown")
                                                                                                   .streetName ("MyStreet")
                                                                                                   .buildingNumber ("22")
                                                                                                   .countryCode ("GR")
                                                                                                   .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                   .postalCode ("11134"))
                                                                              .birthDate (PDTFactory.createLocalDate (1994,
                                                                                                                      Month.FEBRUARY,
                                                                                                                      1))
                                                                              .birthTown ("ATown")
                                                                              .birthName ("John Doe")
                                                                              .familyName ("Doe")
                                                                              .genderCode (EGenderCode.M)
                                                                              .givenName ("John")
                                                                              .id ("LALALA")
                                                                              .idSchemeID ("LALALA"))
                                         .dataSubject (PersonPojo.builder ()
                                                                 .address (AddressPojo.builder ()
                                                                                      .town ("MyTown")
                                                                                      .streetName ("MyStreet")
                                                                                      .buildingNumber ("22")
                                                                                      .countryCode ("GR")
                                                                                      .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                      .postalCode ("11134"))
                                                                 .birthDate (PDTFactory.createLocalDate (1994,
                                                                                                         Month.FEBRUARY,
                                                                                                         1))
                                                                 .birthTown ("ATown")
                                                                 .birthName ("John Doe")
                                                                 .familyName ("Doe")
                                                                 .genderCode (EGenderCode.M)
                                                                 .givenName ("John")
                                                                 .id ("LALALA")
                                                                 .idSchemeID ("LALALA"))
                                         .datasetIdentifier ("IdentifierForDatasets")
                                         .specificationIdentifier ("SpecID")
                                         .consentToken ("AAABBB")
                                         .distribution (DistributionPojo.builder ()
                                                                        .format (EDistributionFormat.STRUCTURED)
                                                                        .mediaType (CMimeType.APPLICATION_PDF))
                                         .build ();
    assertNotNull (request);
  }

  @Test
  public void createEDMConceptRequestLP ()
  {
    final EDMRequest request = EDMRequest.builder ()
                                         .randomID ()
                                         .queryDefinition (EQueryDefinitionType.CONCEPT)
                                         .issueDateTimeNow ()
                                         .procedure (Locale.US, "GBM Procedure")
                                         .dataConsumer (AgentPojo.builder ()
                                                                 .address (AddressPojo.builder ()
                                                                                      .town ("MyTown")
                                                                                      .streetName ("MyStreet")
                                                                                      .buildingNumber ("22")
                                                                                      .countryCode ("GR")
                                                                                      .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                      .postalCode ("11134"))
                                                                 .name ("DC NAME")
                                                                 .id ("1234")
                                                                 .idSchemeID ("VAT"))
                                         .authorizedRepresentative (PersonPojo.builder ()
                                                                              .address (AddressPojo.builder ()
                                                                                                   .town ("MyTown")
                                                                                                   .streetName ("MyStreet")
                                                                                                   .buildingNumber ("22")
                                                                                                   .countryCode ("GR")
                                                                                                   .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                   .postalCode ("11134"))
                                                                              .birthDate (PDTFactory.createLocalDate (1994,
                                                                                                                      Month.FEBRUARY,
                                                                                                                      1))
                                                                              .birthTown ("ATown")
                                                                              .birthName ("John Doe")
                                                                              .familyName ("Doe")
                                                                              .genderCode (EGenderCode.M)
                                                                              .givenName ("John")
                                                                              .id ("LALALA")
                                                                              .idSchemeID ("LALALA"))
                                         .dataSubject (BusinessPojo.builder ()
                                                                   .address (AddressPojo.builder ()
                                                                                        .town ("MyTown")
                                                                                        .streetName ("MyStreet")
                                                                                        .buildingNumber ("22")
                                                                                        .countryCode ("GR")
                                                                                        .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                        .postalCode ("11134"))
                                                                   .legalID ("Niar")
                                                                   .legalIDSchemeID ("Tsiou")
                                                                   .legalName ("NiarTsiou")
                                                                   .id ("anID"))
                                         .datasetIdentifier ("IdentifierForDatasets")
                                         .specificationIdentifier ("SpecID")
                                         .consentToken ("AAABBB")
                                         .concept (ConceptPojo.builder ()
                                                              .name (EToopConcept.COMPANY_TYPE)
                                                              .addChild (ConceptPojo.builder ()
                                                                                    .name (EToopConcept.COMPANY_NAME))
                                                              .addChild (ConceptPojo.builder ()
                                                                                    .name (EToopConcept.COMPANY_CODE))
                                                              .addChild (ConceptPojo.builder ()
                                                                                    .name (EToopConcept.COMPANY_TYPE)))
                                         .build ();
    assertNotNull (request);
  }

  public void createInvalidEDMRequest ()
  {
    // This attempts to create an EDMRequest with both concept and distribution
    // which is not permitted and fails
    final EDMRequest request = new EDMRequest.Builder ().queryDefinition (EQueryDefinitionType.CONCEPT)
                                                        .issueDateTimeNow ()
                                                        .dataConsumer (AgentPojo.builder ()
                                                                                .address (AddressPojo.builder ()
                                                                                                     .town ("MyTown")
                                                                                                     .streetName ("MyStreet")
                                                                                                     .buildingNumber ("22")
                                                                                                     .countryCode ("GR")
                                                                                                     .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                     .postalCode ("11134"))
                                                                                .name ("DC NAME")
                                                                                .id ("1234")
                                                                                .idSchemeID ("VAT"))
                                                        .authorizedRepresentative (PersonPojo.builder ()
                                                                                             .address (AddressPojo.builder ()
                                                                                                                  .town ("MyTown")
                                                                                                                  .streetName ("MyStreet")
                                                                                                                  .buildingNumber ("22")
                                                                                                                  .countryCode ("GR")
                                                                                                                  .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                                  .postalCode ("11134"))
                                                                                             .birthDate (PDTFactory.createLocalDate (1994,
                                                                                                                                     Month.FEBRUARY,
                                                                                                                                     1))
                                                                                             .birthTown ("ATown")
                                                                                             .birthName ("John Doe")
                                                                                             .familyName ("Doe")
                                                                                             .genderCode (EGenderCode.M)
                                                                                             .givenName ("John")
                                                                                             .id ("LALALA")
                                                                                             .idSchemeID ("LALALA"))
                                                        .dataSubject (PersonPojo.builder ()
                                                                                .address (AddressPojo.builder ()
                                                                                                     .town ("MyTown")
                                                                                                     .streetName ("MyStreet")
                                                                                                     .buildingNumber ("22")
                                                                                                     .countryCode ("GR")
                                                                                                     .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                                                     .postalCode ("11134"))
                                                                                .birthDate (PDTFactory.createLocalDate (1994,
                                                                                                                        Month.FEBRUARY,
                                                                                                                        1))
                                                                                .birthTown ("ATown")
                                                                                .birthName ("John Doe")
                                                                                .familyName ("Doe")
                                                                                .genderCode (EGenderCode.M)
                                                                                .givenName ("John")
                                                                                .id ("LALALA")
                                                                                .idSchemeID ("LALALA"))
                                                        .datasetIdentifier ("IdentifierForDatasets")
                                                        .specificationIdentifier ("SpecID")
                                                        .consentToken ("AAABBB")
                                                        .distribution (DistributionPojo.builder ()
                                                                                       .format (EDistributionFormat.STRUCTURED)
                                                                                       .mediaType (CMimeType.APPLICATION_PDF))
                                                        .concept (ConceptPojo.builder ()
                                                                             .name (EToopConcept.COMPANY_TYPE)
                                                                             .addChild (ConceptPojo.builder ()
                                                                                                   .name (EToopConcept.COMPANY_NAME))
                                                                             .addChild (ConceptPojo.builder ()
                                                                                                   .name (EToopConcept.COMPANY_CODE))
                                                                             .addChild (ConceptPojo.builder ()
                                                                                                   .name (EToopConcept.COMPANY_TYPE)))
                                                        .build ();
  }

  @Test
  public void testEDMConceptRequestExport () throws JAXBException, XMLStreamException
  {
    final EDMRequest aReq = EDMExtractors.extractEDMRequest (ClassPathResource.getInputStream ("Concept Request_LP.xml"));
    assertNotNull (aReq);

    final byte [] aBytes = aReq.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMRequest aReq2 = EDMExtractors.extractEDMRequest (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aReq, aReq2);
  }

  @Test
  public void testEDMDocumentRequestExport () throws JAXBException, XMLStreamException
  {
    final EDMRequest aReq = EDMExtractors.extractEDMRequest (ClassPathResource.getInputStream ("Concept Request_NP.xml"));
    assertNotNull (aReq);

    final byte [] aBytes = aReq.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMRequest aReq2 = EDMExtractors.extractEDMRequest (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aReq, aReq2);
  }

  @Test
  public void checkConsistencyConceptRequest () throws JAXBException, XMLStreamException, FileNotFoundException
  {
    final EDMRequest aReq = EDMExtractors.extractEDMRequest (ClassPathResource.getAsFile ("Concept Request_NP.xml"));
    assertNotNull (aReq);

    final byte [] aBytes = aReq.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMRequest aReq2 = EDMExtractors.extractEDMRequest (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aReq, aReq2);
  }

  @Test
  public void checkConsistencyDocumentRequest () throws JAXBException, XMLStreamException, FileNotFoundException
  {
    final EDMRequest aReq = EDMExtractors.extractEDMRequest (ClassPathResource.getAsFile ("Concept Request_LP.xml"));
    assertNotNull (aReq);

    final byte [] aBytes = aReq.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    final EDMRequest aReq2 = EDMExtractors.extractEDMRequest (new NonBlockingByteArrayInputStream (aBytes));
    assertEquals (aReq, aReq2);
  }
}
