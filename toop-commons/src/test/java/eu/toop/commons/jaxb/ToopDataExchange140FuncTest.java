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
package eu.toop.commons.jaxb;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xml.serialize.write.XMLWriter;
import com.helger.xml.serialize.write.XMLWriterSettings;

import eu.toop.commons.concept.EConceptType;
import eu.toop.commons.dataexchange.v140.ObjectFactory;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEAddressWithLOAType;
import eu.toop.commons.dataexchange.v140.TDEConceptRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataConsumerType;
import eu.toop.commons.dataexchange.v140.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDENaturalPersonType;
import eu.toop.commons.dataexchange.v140.TDERoutingInformationType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.BinaryObjectType;

/**
 * Test class for {@link ToopReader} and {@link ToopWriter}.
 *
 * @author Philip Helger
 */
public final class ToopDataExchange140FuncTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToopDataExchange140FuncTest.class);

  @Test
  public void testReadWriteDataRequest ()
  {
    for (final String sFilename : new String [] { "PA2-ConnectedCompanyData/data-request-document-example.xml",
                                                  "PA2-ConnectedCompanyData/data-request-example.xml",
                                                  "PA3-Maritime/MARITIME-STEP1a-request-list-of-ship-certificates.xml",
                                                  "PA3-Maritime/MARITIME-STEP2a-data-request-document-example.xml",
                                                  "commander-request1.xml",
                                                  "data-request1.xml" })
    {
      final TDETOOPRequestType aRequest = ToopReader.request140 ()
                                                    .read (new File ("src/test/resources/xml/1.4.1/" + sFilename));
      assertNotNull (aRequest);
      final String sXML = ToopWriter.request140 ().getAsString (aRequest);
      assertNotNull (sXML);
    }
  }

  @Test
  public void testReadWriteDataResponse ()
  {
    for (final String sFilename : new String [] { "PA2-ConnectedCompanyData/data-response-document-example.xml",
                                                  "PA2-ConnectedCompanyData/data-response-example.xml",
                                                  "PA2-ConnectedCompanyData/data-response-with-ERROR-example.xml",
                                                  "PA3-Maritime/MARITIME-STEP1b-response-list-of-ship-certificates.xml",
                                                  "PA3-Maritime/MARITIME-STEP2b-data-response-document-example.xml",
                                                  "data-response-error1.xml",
                                                  "data-response1.xml" })
    {
      final TDETOOPResponseType aResponse = ToopReader.response140 ()
                                                      .read (new File ("src/test/resources/xml/1.4.1/" + sFilename));
      assertNotNull (aResponse);
      final String sXML = ToopWriter.response140 ().getAsString (aResponse);
      assertNotNull (sXML);
    }
  }

  @Test
  public void testCreateRequestFromScratch ()
  {
    final TDETOOPRequestType r = new TDETOOPRequestType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifierUUID ());
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper140.createIndicator (false));
    r.setSpecificationIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper140.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
    {
      final TDERoutingInformationType ri = new TDERoutingInformationType ();
      // Document type ID
      ri.setDocumentTypeIdentifier (ToopXSDHelper140.createIdentifier ("toop-doctypeid",
                                                                       "data.request.registeredorganization"));
      // Process ID
      ri.setProcessIdentifier (ToopXSDHelper140.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
      ri.setDataConsumerCountryCode (ToopXSDHelper140.createCode ("AT"));
      ri.setDataConsumerElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("iso6523-actorid-upis",
                                                                                        "9915:test"));
      // Destination country code
      ri.setDataProviderCountryCode (ToopXSDHelper140.createCode ("AT"));
      r.setRoutingInformation (ri);
    }
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper140.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper140.createText ("Helger Enterprises"));
      // Sender participant ID
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA ("bla"));
        aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA ("Helger"));
        aNP.setFirstName (ToopXSDHelper140.createTextWithLOA ("Philip"));
        aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataRequestSubject (aDS);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      r.setDataRequestAuthorization (aAuth);
    }
    {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper140.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.TC.getID ()));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier ("toopUri"));
          aToopConcept.setConceptName (ToopXSDHelper140.createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    final Document aDoc = ToopWriter.request140 ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true)
    {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._Request_QNAME.getNamespaceURI ());
      LOGGER.info (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }

  @Test
  public void testCreateResponseFromScratch ()
  {
    final TDETOOPResponseType r = new TDETOOPResponseType ();
    r.setDocumentUniversalUniqueIdentifier (ToopXSDHelper140.createIdentifierUUID ());
    r.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
    r.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
    r.setCopyIndicator (ToopXSDHelper140.createIndicator (false));
    r.setSpecificationIdentifier (ToopXSDHelper140.createSpecificationIdentifierResponse ());
    r.setDataConsumerDocumentIdentifier (ToopXSDHelper140.createIdentifier ("DC-ID-17"));
    r.setDataRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
    {
      final TDERoutingInformationType ri = new TDERoutingInformationType ();
      // Document type ID
      ri.setDocumentTypeIdentifier (ToopXSDHelper140.createIdentifier ("toop-doctypeid",
                                                                       "data.request.registeredorganization"));
      // Process ID
      ri.setProcessIdentifier (ToopXSDHelper140.createIdentifier ("toop-procid", "urn:toop:www.toop.eu/data-request"));
      ri.setDataConsumerCountryCode (ToopXSDHelper140.createCode ("AT"));
      ri.setDataConsumerElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("iso6523-actorid-upis",
                                                                                        "9915:test"));
      // Destination country code
      ri.setDataProviderCountryCode (ToopXSDHelper140.createCode ("XK"));
      ri.setDataProviderElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("me@register.example.org"));
      r.setRoutingInformation (ri);
    }
    {
      final TDEDataConsumerType aDC = new TDEDataConsumerType ();
      aDC.setDCUniqueIdentifier (ToopXSDHelper140.createIdentifier ("ATU12345678"));
      aDC.setDCName (ToopXSDHelper140.createText ("Helger Enterprises"));
      // Sender participant ID
      final TDEAddressType aAddress = new TDEAddressType ();
      aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("AT"));
      aDC.setDCLegalAddress (aAddress);
      r.setDataConsumer (aDC);
    }
    {
      final TDEDataRequestSubjectType aDS = new TDEDataRequestSubjectType ();
      aDS.setDataRequestSubjectTypeCode (ToopXSDHelper140.createCode ("12345"));
      {
        final TDENaturalPersonType aNP = new TDENaturalPersonType ();
        aNP.setPersonIdentifier (ToopXSDHelper140.createIdentifierWithLOA ("bla"));
        aNP.setFamilyName (ToopXSDHelper140.createTextWithLOA ("Helger"));
        aNP.setFirstName (ToopXSDHelper140.createTextWithLOA ("Philip"));
        aNP.setBirthDate (ToopXSDHelper140.createDateWithLOANow ());
        final TDEAddressWithLOAType aAddress = new TDEAddressWithLOAType ();
        // Destination country to use
        aAddress.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("DE"));
        aNP.setNaturalPersonLegalAddress (aAddress);
        aDS.setNaturalPerson (aNP);
      }
      r.setDataRequestSubject (aDS);
    }
    {
      final TDEDataRequestAuthorizationType aAuth = new TDEDataRequestAuthorizationType ();
      final BinaryObjectType aBO = new BinaryObjectType ();
      aBO.setValue ("11101010101010001110101".getBytes (StandardCharsets.ISO_8859_1));
      aBO.setMimeCode ("application/octet-stream");
      aAuth.setDataRequestConsentToken (aBO);
      r.setDataRequestAuthorization (aAuth);
    }
    {
      final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
      aReq.setDataElementRequestIdentifier (ToopXSDHelper140.createIdentifier ("bla"));
      {
        final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
        aSrcConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.DC.getID ()));
        aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
        aSrcConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier ("elUri"));
        aSrcConcept.setConceptName (ToopXSDHelper140.createText ("elType"));

        {
          final TDEConceptRequestType aToopConcept = new TDEConceptRequestType ();
          aToopConcept.setConceptTypeCode (ToopXSDHelper140.createCode (EConceptType.TC.getID ()));
          aToopConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper140.createIndicator (false));
          aToopConcept.setConceptNamespace (ToopXSDHelper140.createIdentifier ("toopUri"));
          aToopConcept.setConceptName (ToopXSDHelper140.createText ("toopType"));
          aSrcConcept.addConceptRequest (aToopConcept);
        }
        aReq.setConceptRequest (aSrcConcept);
      }

      r.addDataElementRequest (aReq);
    }

    {
      final TDEDataProviderType p = new TDEDataProviderType ();
      p.setDPIdentifier (ToopXSDHelper140.createIdentifier ("atbla"));
      p.setDPName (ToopXSDHelper140.createText ("Register1"));
      final TDEAddressType pa = new TDEAddressType ();
      pa.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("XK"));
      p.setDPLegalAddress (pa);
      r.addDataProvider (p);
    }

    final Document aDoc = ToopWriter.response140 ().getAsDocument (r);
    assertNotNull (aDoc);
    if (true)
    {
      final MapBasedNamespaceContext aCtx = new MapBasedNamespaceContext ();
      aCtx.addMapping ("toop", ObjectFactory._Request_QNAME.getNamespaceURI ());
      LOGGER.info (XMLWriter.getNodeAsString (aDoc, new XMLWriterSettings ().setNamespaceContext (aCtx)));
    }
  }
}
