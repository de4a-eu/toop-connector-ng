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
package eu.toop.commons.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.helger.asic.SignatureHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.math.MathHelper;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.security.keystore.EKeyStoreType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEAddressType;
import eu.toop.commons.dataexchange.v140.TDEDataElementResponseValueType;
import eu.toop.commons.dataexchange.v140.TDEDataProviderType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.jaxb.ToopXSDHelper140;

/**
 * Test class for class {@link ToopMessageBuilder140}.
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class ToopMessageBuilder140Test
{
  private static final SignatureHelper SH = new SignatureHelper (EKeyStoreType.JKS,
                                                                 "playground-keystore-v1.jks",
                                                                 "toop4eu",
                                                                 "sms-key",
                                                                 "toop4eu");

  @Test
  public void testRequestMessage () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder140.createMockRequest (ToopMessageBuilder140.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                          sDPCountryCode,
                                                                                                                                          true,
                                                                                                                                          "id"),
                                                                                      sDCCountryCode,
                                                                                      sDPCountryCode,
                                                                                      ToopXSDHelper140.createIdentifier ("toop",
                                                                                                                         "senderid"),
                                                                                      EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                      EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                      new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                 "Acme Inc.")));
      ToopMessageBuilder140.createRequestMessageAsic (aSrcRequest, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcRequest, aSrcRequest.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final ICommonsList <AsicReadEntry> aAttachments = new CommonsArrayList <> ();
        final TDETOOPRequestType aRead = ToopMessageBuilder140.parseRequestMessage (archiveInput, aAttachments::add);
        assertNotNull (aRead);
        assertTrue (aAttachments.isEmpty ());

        assertEquals (aRead, aSrcRequest);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  public void testResponseMessage () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder140.createMockResponse (ToopXSDHelper140.createIdentifier ("toop",
                                                                                                                            "senderid"),
                                                                                         ToopMessageBuilder140.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                             sDPCountryCode,
                                                                                                                                             true,
                                                                                                                                             "id"),
                                                                                         sDCCountryCode,
                                                                                         sDPCountryCode,
                                                                                         EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                         EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                         new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                    "Acme Inc.")));
      ToopMessageBuilder140.createResponseMessageAsic (aSrcResponse, aBAOS, SH);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());

      try (final NonBlockingByteArrayInputStream archiveInput = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder140.parseResponseMessage (archiveInput, null);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  public void testResponseMessageV2 () throws ToopErrorException, IOException
  {
    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      final String sDCCountryCode = "SE";
      final String sDPCountryCode = "SE";
      final TDETOOPRequestType aSrcRequest = ToopMessageBuilder140.createMockResponse (ToopXSDHelper140.createIdentifier ("toop",
                                                                                                                          "senderid"),
                                                                                       ToopMessageBuilder140.createMockDataRequestSubject (sDCCountryCode,
                                                                                                                                           sDPCountryCode,
                                                                                                                                           true,
                                                                                                                                           "id"),
                                                                                       sDCCountryCode,
                                                                                       sDPCountryCode,
                                                                                       EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION,
                                                                                       EPredefinedProcessIdentifier.DATAREQUESTRESPONSE,
                                                                                       new CommonsArrayList <> (new ConceptValue ("companyName",
                                                                                                                                  "Acme Inc."),
                                                                                                                new ConceptValue ("companyVATIN",
                                                                                                                                  "blafoo.vatin")));
      final TDETOOPResponseType aSrcResponse = ToopMessageBuilder140.createResponse (aSrcRequest);
      {
        aSrcRequest.getRoutingInformation ()
                   .setDataProviderElectronicAddressIdentifier (ToopXSDHelper140.createIdentifier ("elonia@register.example.org"));

        // Required for response
        final TDEDataProviderType p = new TDEDataProviderType ();
        p.setDPIdentifier (ToopXSDHelper140.createIdentifier ("toop", "blafoo-elonia"));
        p.setDPName (ToopXSDHelper140.createText ("EloniaDP"));
        final TDEAddressType pa = new TDEAddressType ();
        pa.setCountryCode (ToopXSDHelper140.createCodeWithLOA ("SV"));
        p.setDPLegalAddress (pa);
        aSrcResponse.addDataProvider (p);
      }
      // Add response to concept 1/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper140.createIndicator (false));
        aResponseValue.setResponseIdentifier (ToopXSDHelper140.createIdentifier ("id4711"));
        aSrcResponse.getDataElementRequestAtIndex (0).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      // Add response to concept 2/2
      {
        final TDEDataElementResponseValueType aResponseValue = new TDEDataElementResponseValueType ();
        aResponseValue.setErrorIndicator (ToopXSDHelper140.createIndicator (false));
        aResponseValue.setAlternativeResponseIndicator (ToopXSDHelper140.createIndicator (false));
        aResponseValue.setResponseNumeric (ToopXSDHelper140.createNumeric (MathHelper.toBigDecimal (47.11)));
        aSrcResponse.getDataElementRequestAtIndex (1).getConceptRequest ().addDataElementResponseValue (aResponseValue);
      }
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aSrcResponse, aSrcResponse.clone ());
      ToopMessageBuilder140.createResponseMessageAsic (aSrcResponse, aBAOS, SH);

      try (final NonBlockingByteArrayInputStream aAsicIS = aBAOS.getAsInputStream ())
      {
        // Read ASIC again
        final TDETOOPResponseType aRead = ToopMessageBuilder140.parseResponseMessage (aAsicIS, null);
        assertNotNull (aRead);

        assertEquals (aRead, aSrcResponse);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }

      // Read with attachments as well
      try (final NonBlockingByteArrayInputStream aAsicIS = aBAOS.getAsInputStream ())
      {
        // Read ASIC again with attachments
        final ICommonsList <AsicReadEntry> aList = new CommonsArrayList <> ();
        final TDETOOPResponseType aRead = ToopMessageBuilder140.parseResponseMessage (aAsicIS, aList::add);
        assertNotNull (aRead);
        assertEquals (0, aList.size ());

        assertEquals (aRead, aSrcResponse);
        CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRead, aRead.clone ());
      }
    }
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testEmpty () throws ToopErrorException
  {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ())
    {
      // No data
      ToopMessageBuilder140.createRequestMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ())
    {
      // No data
      ToopMessageBuilder140.createResponseMessageAsic (null, archiveOutput, SH);
      fail ("Exception expected");
    }
    catch (final NullPointerException ex)
    {
      // Expected
    }
  }
}
