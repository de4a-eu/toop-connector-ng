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
package eu.toop.edm.creator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Month;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.mime.CMimeType;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.EDMRequest;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.model.EDistributionFormat;
import eu.toop.edm.model.EGenderCode;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.schematron.SchematronEDM2Validator;

/**
 * Test class for class {@link EDMRequestCreator}
 *
 * @author Philip Helger
 */
public final class EDMRequestCreatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EDMRequestCreatorTest.class);

  @Nonnull
  private static BusinessPojo _dsBusiness ()
  {
    return BusinessPojo.builder ()
                       .legalID ("DE730757727")
                       .legalIDSchemeID ("VAT")
                       .id ("DE/GB/02735442Z")
                       .idSchemeID ("EIDAS")
                       .legalName ("AnyCompanyName")
                       .address (AddressPojo.builder ()
                                            .fullAddress ("Prince Street 15")
                                            .streetName ("Prince Street")
                                            .buildingNumber ("15")
                                            .town ("Liverpool")
                                            .postalCode ("15115")
                                            .countryCode ("GB"))
                       .build ();
  }

  @Nonnull
  private static PersonPojo _dsPerson ()
  {
    return PersonPojo.builder ()
                     .id ("1212")
                     .idSchemeID ("VAT")
                     .familyName ("Sabatini")
                     .givenName ("Bianca")
                     .genderCode (EGenderCode.F)
                     .birthName ("Bianca Sabatini")
                     .birthDate (PDTFactory.createLocalDate (1998, Month.JANUARY, 1))
                     .birthTown ("London")
                     .address (AddressPojo.builder ()
                                          .fullAddress ("Prince Street 15")
                                          .streetName ("Prince Street")
                                          .buildingNumber ("15")
                                          .town ("Liverpool")
                                          .postalCode ("15115")
                                          .countryCode ("GB"))
                     .build ();
  }

  @Nonnull
  private static EDMRequest.Builder _builderConcept ()
  {
    final String sConceptNS = "http://toop.eu/registered-organization";
    return EDMRequest.builderConcept ()
                     .randomID ()
                     .issueDateTime (PDTFactory.createLocalDateTime (2020, Month.FEBRUARY, 14, 19, 20, 30))
                     .procedure (Locale.UK, "GBM Procedure")
                     .fullfillingRequirement (null)
                     .consentToken ("MTExMDEwMTAxMDEwMTAwMDExMTAxMDE=")
                     .datasetIdentifier ("DATASETIDENTIFIER")
                     .dataConsumer (AgentPojo.builder ()
                                             .id ("DE730757727")
                                             .idSchemeID ("VAT")
                                             .name ("aCompanyName")
                                             .address (AddressPojo.builder ()
                                                                  .fullAddress ("Prince Street 15")
                                                                  .streetName ("Prince Street")
                                                                  .buildingNumber ("15")
                                                                  .town ("Liverpool")
                                                                  .postalCode ("15115")
                                                                  .countryCode ("GB")))
                     .authorizedRepresentative (PersonPojo.builder ()
                                                          .id ("1515")
                                                          .idSchemeID ("VAT")
                                                          .familyName ("Smith")
                                                          .givenName ("Mark")
                                                          .genderCode (EGenderCode.M)
                                                          .birthName ("Mark Smith")
                                                          .birthDate (PDTFactory.createLocalDate (1990,
                                                                                                  Month.JANUARY,
                                                                                                  1))
                                                          .address (AddressPojo.builder ()
                                                                               .fullAddress ("Some other 15")
                                                                               .streetName ("Some other")
                                                                               .buildingNumber ("15")
                                                                               .town ("Liverpool")
                                                                               .postalCode ("15115")
                                                                               .countryCode ("GB")))
                     .concept (ConceptPojo.builder ()
                                          .id ("ConceptID-1")
                                          .name (sConceptNS, "CompanyData")
                                          .addChild (ConceptPojo.builder ()
                                                                .id ("ConceptID-2")
                                                                .name (sConceptNS, "Concept-Name-2"))
                                          .addChild (ConceptPojo.builder ()
                                                                .id ("ConceptID-3")
                                                                .name (sConceptNS, "Concept-Name-3")
                                                                .addChild (ConceptPojo.builder ()
                                                                                      .id ("ConceptID-31")
                                                                                      .name (sConceptNS,
                                                                                             "Concept-Name-31"))
                                                                .addChild (ConceptPojo.builder ()
                                                                                      .id ("ConceptID-32")
                                                                                      .name (sConceptNS,
                                                                                             "Concept-Name-32")))
                                          .addChild (ConceptPojo.builder ()
                                                                .id ("ConceptID-4")
                                                                .name (sConceptNS, "Concept-Name-4")));
  }

  @Test
  public void testRequestConceptLegalPerson ()
  {
    final EDMRequest aRequest = _builderConcept ().dataSubject (_dsBusiness ()).build ();
    assertNotNull (aRequest);

    final String sXML = aRequest.getWriter ().getAsString ();
    assertNotNull (sXML);

    if (false)
      LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aRequest.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Test
  public void testRequestConceptNaturalPerson ()
  {
    final EDMRequest aRequest = _builderConcept ().dataSubject (_dsPerson ()).build ();
    assertNotNull (aRequest);

    final String sXML = aRequest.getWriter ().getAsString ();
    assertNotNull (sXML);

    if (true)
      LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aRequest.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Nonnull
  private static EDMRequest.Builder _builderDocument ()
  {
    return EDMRequest.builderDocument ()
                     .randomID ()
                     .issueDateTime (PDTFactory.createLocalDateTime (2020, Month.FEBRUARY, 14, 19, 20, 30))
                     .procedure (Locale.UK, "GBM Procedure")
                     .fullfillingRequirement (null)
                     .consentToken ("MTExMDEwMTAxMDEwMTAwMDExMTAxMDE=")
                     .datasetIdentifier ("DATASETIDENTIFIER")
                     .dataConsumer (AgentPojo.builder ()
                                             .id ("DE730757727")
                                             .idSchemeID ("VAT")
                                             .name ("aCompanyName")
                                             .address (AddressPojo.builder ()
                                                                  .fullAddress ("Prince Street 15")
                                                                  .streetName ("Prince Street")
                                                                  .buildingNumber ("15")
                                                                  .town ("Liverpool")
                                                                  .postalCode ("15115")
                                                                  .countryCode ("GB")))
                     .authorizedRepresentative (PersonPojo.builder ()
                                                          .id ("1515")
                                                          .idSchemeID ("VAT")
                                                          .familyName ("Smith")
                                                          .givenName ("Mark")
                                                          .genderCode (EGenderCode.M)
                                                          .birthName ("Mark Smith")
                                                          .birthDate (PDTFactory.createLocalDate (1990,
                                                                                                  Month.JANUARY,
                                                                                                  1))
                                                          .address (AddressPojo.builder ()
                                                                               .fullAddress ("Some other 15")
                                                                               .streetName ("Some other")
                                                                               .buildingNumber ("15")
                                                                               .town ("Liverpool")
                                                                               .postalCode ("15115")
                                                                               .countryCode ("GB")))
                     .distribution (DistributionPojo.builder ()
                                                    .format (EDistributionFormat.UNSTRUCTURED)
                                                    .mediaType (CMimeType.APPLICATION_PDF));
  }

  @Test
  public void testRequestDocumentLegalPerson ()
  {
    final EDMRequest aRequest = _builderDocument ().dataSubject (_dsBusiness ()).build ();
    assertNotNull (aRequest);

    final String sXML = aRequest.getWriter ().getAsString ();
    assertNotNull (sXML);

    if (false)
      LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aRequest.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Test
  public void testRequestDocumentNaturalPerson ()
  {
    final EDMRequest aRequest = _builderDocument ().dataSubject (_dsPerson ()).build ();
    assertNotNull (aRequest);

    final String sXML = aRequest.getWriter ().getAsString ();
    assertNotNull (sXML);

    if (false)
      LOGGER.info (sXML);

    {
      // Schematron validation
      final Document aDoc = aRequest.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Test
  public void testRequestConceptMissingSlots ()
  {
    try
    {
      _builderConcept ().specificationIdentifier (null).build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {
      // Expected
    }
    try
    {
      _builderConcept ().issueDateTime (null).build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {
      // Expected
    }
  }
}
