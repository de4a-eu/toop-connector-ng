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
package eu.toop.commons.schematron;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.commons.asic.ToopMessageBuilder140;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.pilot.gbm.EToopConcept;
import eu.toop.commons.xml.ToopWriter;
import eu.toop.commons.xml.ToopXSDHelper140;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

/**
 * Test class for class {@link TOOPSchematron140Validator}.
 *
 * @author Philip Helger
 */
public final class TOOPSchematron140ValidatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPSchematron140ValidatorTest.class);

  @Test
  public void testBasic ()
  {
    final TOOPSchematron140Validator v = new TOOPSchematron140Validator ();

    for (final String sFilename : new String [] { "PA2-ConnectedCompanyData/data-request-document-example.xml",
                                                  "PA2-ConnectedCompanyData/data-request-example.xml",
                                                  "PA2-ConnectedCompanyData/data-response-document-example.xml",
                                                  "PA2-ConnectedCompanyData/data-response-example.xml",
                                                  "PA2-ConnectedCompanyData/data-response-with-ERROR-example.xml",
                                                  "PA3-Maritime/MARITIME-STEP1a-request-list-of-ship-certificates.xml",
                                                  "PA3-Maritime/MARITIME-STEP1b-response-list-of-ship-certificates.xml",
                                                  "PA3-Maritime/MARITIME-STEP2a-data-request-document-example.xml",
                                                  "PA3-Maritime/MARITIME-STEP2b-data-response-document-example.xml",
                                                  "data-response-example2.xml",
                                                  "data-response-example3.xml",
                                                  "example-request-se-sk.xml",
                                                  "ToopRequest_7d5cc223-033e-4eda-9dcc-1174193cdc05.xml",
                                                  "ToopResponse_cf172cf7-09f7-4a33-8a41-34106990152a.xml" })
    {
      LOGGER.info ("Checking " + sFilename);

      final FileSystemResource aFS = new FileSystemResource ("src/test/resources/xml/1.4.1/" + sFilename);
      assertTrue (aFS.exists ());

      final ICommonsList <AbstractSVRLMessage> aFAs = v.validateTOOPMessage (aFS);
      assertNotNull (aFAs);
      for (final AbstractSVRLMessage aFA : aFAs)
        LOGGER.info (aFA.toString ());
      assertTrue (aFAs.isEmpty ());
    }
  }

  @SuppressWarnings ("deprecation")
  @Test
  public void testValidateMockRequest ()
  {
    final String sDCCountryCode = "IT";
    final String sDPCountryCode = "AT";
    final TDEDataRequestSubjectType aRequestSubject = ToopMessageBuilder140.createMockDataRequestSubject (sDCCountryCode,
                                                                                                          sDPCountryCode,
                                                                                                          false,
                                                                                                          "id");
    final IdentifierType aSenderParticipantID = ToopXSDHelper140.createIdentifier ("iso6523-actorid-upis", "9915:bla");

    // Query all
    final ICommonsList <ConceptValue> aValues = new CommonsArrayList <> ();
    for (final EToopConcept e : EToopConcept.values ())
      aValues.add (e.getAsConceptValue ());

    final TDETOOPRequestType aRequestMsg = ToopMessageBuilder140.createMockRequest (aRequestSubject,
                                                                                    sDCCountryCode,
                                                                                    sDPCountryCode,
                                                                                    aSenderParticipantID,
                                                                                    EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40,
                                                                                    EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                    aValues);
    assertNotNull (aRequestMsg);

    final Document aDoc = ToopWriter.request140 ().getAsDocument (aRequestMsg);
    assertNotNull (aDoc);

    // Schematron validation
    final TOOPSchematron140Validator aValidator = new TOOPSchematron140Validator ();
    final ICommonsList <AbstractSVRLMessage> aMsgs = aValidator.validateTOOPMessage (aDoc);
    for (final AbstractSVRLMessage aMsg : aMsgs)
      assertTrue (aMsg.getFlag () == EErrorLevel.WARN);
  }

  @SuppressWarnings ("deprecation")
  @Test
  public void testValidateMockResponse ()
  {
    final String sDCCountryCode = "IT";
    final String sDPCountryCode = "AT";
    final TDEDataRequestSubjectType aRequestSubject = ToopMessageBuilder140.createMockDataRequestSubject (sDCCountryCode,
                                                                                                          sDPCountryCode,
                                                                                                          false,
                                                                                                          "id");
    final IdentifierType aSenderParticipantID = ToopXSDHelper140.createIdentifier ("iso6523-actorid-upis", "9915:bla");

    // Query all
    final ICommonsList <ConceptValue> aValues = new CommonsArrayList <> ();
    for (final EToopConcept e : EToopConcept.values ())
      aValues.add (e.getAsConceptValue ());

    final TDETOOPResponseType aResponseMsg = ToopMessageBuilder140.createMockResponse (aSenderParticipantID,
                                                                                       aRequestSubject,
                                                                                       sDCCountryCode,
                                                                                       sDPCountryCode,
                                                                                       EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40,
                                                                                       EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                       aValues);
    assertNotNull (aResponseMsg);

    final Document aDoc = ToopWriter.response140 ().getAsDocument (aResponseMsg);
    assertNotNull (aDoc);

    // Schematron validation
    final TOOPSchematron140Validator aValidator = new TOOPSchematron140Validator ();
    final ICommonsList <AbstractSVRLMessage> aMsgs = aValidator.validateTOOPMessage (aDoc);
    for (final AbstractSVRLMessage aMsg : aMsgs)
    {
      LOGGER.info (aMsg.getFlag () + "@" + aMsg.getLocation () + " - " + aMsg.getTest () + " / " + aMsg.getText ());
      assertTrue (aMsg.getFlag () == EErrorLevel.WARN);
    }
  }
}
