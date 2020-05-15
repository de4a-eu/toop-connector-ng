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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Month;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.DocumentReferencePojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.QualifiedRelationPojo;
import eu.toop.edm.model.RepositoryItemRefPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.edm.schematron.SchematronEDM2Validator;
import eu.toop.regrep.ERegRepResponseStatus;

/**
 * Test class for class {@link EDMResponse}.
 *
 * @author Philip Helger
 */
public final class EDMResponseTest
{
  private static void _testWriteAndRead (@Nonnull final EDMResponse aResp)
  {
    assertNotNull (aResp);

    // Write
    final byte [] aBytes = aResp.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    // Re-read
    final EDMResponse aResp2 = EDMResponse.getReader ().read (aBytes);

    // Compare with original
    assertEquals (aResp, aResp2);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aResp, aResp2);

    {
      // Schematron validation
      final Document aDoc = aResp.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Nonnull
  private static EDMResponse.Builder _resp ()
  {
    return EDMResponse.builder ()
                      .requestID (UUID.randomUUID ())
                      .issueDateTimeNow ()
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
                      .specificationIdentifier ("Niar");
  }

  @Nonnull
  private static EDMResponse.Builder _respConcept ()
  {
    return _resp ().queryDefinition (EQueryDefinitionType.CONCEPT)
                   .concept (ConceptPojo.builder ()
                                        .id ("ConceptID-1")
                                        .name (EToopConcept.REGISTERED_ORGANIZATION)
                                        .addChild (ConceptPojo.builder ()
                                                              .randomID ()
                                                              .name (EToopConcept.COMPANY_NAME)
                                                              .valueText ("Helger Enterprises"))
                                        .addChild (ConceptPojo.builder ()
                                                              .randomID ()
                                                              .name (EToopConcept.FAX_NUMBER)
                                                              .valueText ("342342424"))
                                        .addChild (ConceptPojo.builder ()
                                                              .randomID ()
                                                              .name (EToopConcept.FOUNDATION_DATE)
                                                              .valueDate (PDTFactory.createLocalDate (1960,
                                                                                                      Month.AUGUST,
                                                                                                      12))));
  }

  @Nonnull
  private static DatasetPojo.Builder _dataset ()
  {
    return DatasetPojo.builder ()
                      .description ("bla desc")
                      .title ("bla title")
                      .distribution (DocumentReferencePojo.builder ()
                                                          .documentURI ("URI")
                                                          .documentDescription ("DocumentDescription")
                                                          .documentType ("docType")
                                                          .localeCode ("GR"))
                      .creator (AgentPojo.builder ()
                                         .name ("Agent name")
                                         .address (AddressPojo.builder ().town ("Kewlkidshome")))
                      .ids ("RE238918378", "DOC-555")
                      .issuedNow ()
                      .language ("en")
                      .lastModifiedNow ()
                      .validFrom (PDTFactory.getCurrentLocalDate ().minusMonths (1))
                      .validTo (PDTFactory.getCurrentLocalDate ().plusYears (1))
                      .qualifiedRelation (QualifiedRelationPojo.builder ()
                                                               .description ("LegalResourceDesc")
                                                               .title ("Name")
                                                               .id ("RE238918378"));
  }

  @Nonnull
  private static EDMResponse.Builder _respDocument ()
  {
    return _resp ().queryDefinition (EQueryDefinitionType.DOCUMENT)
                   .dataset (_dataset ())
                   .repositoryItemRef (RepositoryItemRefPojo.builder ()
                                                            .title ("Evidence.pdf")
                                                            .link ("https://www.example.com/evidence.pdf"));
  }

  @Nonnull
  private static EDMResponse.Builder _respDocumentRef ()
  {
    return _resp ().queryDefinition (EQueryDefinitionType.OBJECTREF).dataset (_dataset ());
  }

  @Test
  public void createConceptResponse ()
  {
    final EDMResponse aResp = EDMResponse.getReader ().read (ClassPathResource.getInputStream ("Concept Response.xml"));
    _testWriteAndRead (aResp);
  }

  @Test
  public void createDocumentResponse ()
  {
    final EDMResponse aResp = _respDocument ().build ();
    _testWriteAndRead (aResp);
  }

  @Test
  public void createDocumentRefResponse ()
  {
    final EDMResponse aResp = _respDocumentRef ().build ();
    _testWriteAndRead (aResp);
  }

  public void createDocumentResponseWithConceptType ()
  {
    try
    {
      // This attempts to create an EDMResponse with a dataset element but with
      // ConceptQuery set as the QueryDefinition
      // which is not permitted and fails
      _respConcept ().dataset (_dataset ()).build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {
      // Expected
    }
  }

  @Test
  public void testReadAndWriteExampleFiles ()
  {
    EDMResponse aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Concept Response.xml"));
    _testWriteAndRead (aResponse);

    aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Document Response.xml"));
    _testWriteAndRead (aResponse);

    aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Document Response_NoRepositoryItemRef.xml"));
    _testWriteAndRead (aResponse);
  }

  @Test
  public void testBadCases ()
  {
    EDMResponse aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Bogus.xml"));
    assertNull (aResponse);

    aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Concept Request_LP.xml"));
    assertNull (aResponse);
  }
}
