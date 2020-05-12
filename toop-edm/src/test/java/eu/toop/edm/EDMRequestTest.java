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
import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
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
import eu.toop.edm.schematron.SchematronEDM2Validator;

/**
 * Test class for class {@link EDMRequest}.
 *
 * @author Philip Helger
 */
public final class EDMRequestTest
{
  private static void _testWriteAndRead (@Nonnull final EDMRequest aReq)
  {
    assertNotNull (aReq);

    // Write
    final byte [] aBytes = aReq.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    // Re-read
    final EDMRequest aReq2 = EDMRequest.getReader ().read (aBytes);

    // Compare with original
    assertEquals (aReq, aReq2);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aReq, aReq2);

    {
      // Schematron validation
      final Document aDoc = aReq.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Nonnull
  private static EDMRequest.Builder _req ()
  {
    return EDMRequest.builder ()
                     .randomID ()
                     .issueDateTimeNow ()
                     .procedure (Locale.US, "GBM Procedure")
                     .addFullfillingRequirement (new CCCEVRequirementType ())
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
                     .datasetIdentifier ("IdentifierForDatasets")
                     .specificationIdentifier ("SpecID")
                     .consentToken ("AAABBB");
  }

  @Nonnull
  private static EDMRequest.Builder _reqConcept ()
  {
    return _req ().queryDefinition (EQueryDefinitionType.CONCEPT)
                  .concept (ConceptPojo.builder ()
                                       .randomID ()
                                       .name (EToopConcept.COMPANY_TYPE)
                                       .addChild (ConceptPojo.builder ().randomID ().name (EToopConcept.COMPANY_NAME))
                                       .addChild (ConceptPojo.builder ().randomID ().name (EToopConcept.COMPANY_CODE))
                                       .addChild (ConceptPojo.builder ().randomID ().name (EToopConcept.COMPANY_TYPE)));
  }

  @Nonnull
  private static EDMRequest.Builder _reqDocument ()
  {
    return _req ().queryDefinition (EQueryDefinitionType.DOCUMENT)
                  .distribution (DistributionPojo.builder ()
                                                 .format (EDistributionFormat.STRUCTURED)
                                                 .mediaType (CMimeType.APPLICATION_PDF));
  }

  @Nonnull
  private static PersonPojo.Builder _np ()
  {
    return PersonPojo.builder ()
                     .address (AddressPojo.builder ()
                                          .town ("MyTown")
                                          .streetName ("MyStreet")
                                          .buildingNumber ("22")
                                          .countryCode ("GR")
                                          .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                          .postalCode ("11134"))
                     .birthDate (PDTFactory.createLocalDate (1994, Month.FEBRUARY, 1))
                     .birthTown ("ATown")
                     .birthName ("John Doe")
                     .familyName ("Doe")
                     .genderCode (EGenderCode.M)
                     .givenName ("John")
                     .id ("LALALA")
                     .idSchemeID ("LALALA");
  }

  @Nonnull
  private static BusinessPojo.Builder _lp ()
  {
    return BusinessPojo.builder ()
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
                       .id ("anID");
  }

  @Test
  public void createEDMConceptRequestLP ()
  {
    final EDMRequest aRequest = _reqConcept ().dataSubject (_lp ()).build ();
    _testWriteAndRead (aRequest);
  }

  @Test
  public void createEDMConceptRequestNP ()
  {
    final EDMRequest aRequest = _reqConcept ().dataSubject (_np ()).build ();
    _testWriteAndRead (aRequest);
  }

  @Test
  public void createEDMDocumentRequestLP ()
  {
    final EDMRequest aRequest = _reqDocument ().dataSubject (_lp ()).build ();
    _testWriteAndRead (aRequest);
  }

  @Test
  public void createEDMDocumentRequestNP ()
  {
    final EDMRequest aRequest = _reqDocument ().dataSubject (_np ()).build ();
    _testWriteAndRead (aRequest);
  }

  public void createInvalidEDMRequest ()
  {
    try
    {
      // This attempts to create an EDMRequest with both concept and
      // distribution which is not permitted and fails
      _reqConcept ().dataSubject (_np ())
                    .distribution (DistributionPojo.builder ()
                                                   .format (EDistributionFormat.STRUCTURED)
                                                   .mediaType (CMimeType.APPLICATION_PDF))
                    .build ();
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
    EDMRequest aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Concept Request_LP.xml"));
    _testWriteAndRead (aRequest);

    aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Concept Request_NP.xml"));
    _testWriteAndRead (aRequest);

    aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Document Request_LP.xml"));
    _testWriteAndRead (aRequest);

    aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Document Request_NP.xml"));
    _testWriteAndRead (aRequest);
  }

  @Test
  public void testBadCases ()
  {
    EDMRequest aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Bogus.xml"));
    assertNull (aRequest);

    aRequest = EDMRequest.getReader ().read (new ClassPathResource ("Concept Response.xml"));
    assertNull (aRequest);
  }
}
