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
package eu.toop.edm.regrep;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.datetime.util.PDTXMLConverter;
import com.helger.xml.serialize.read.DOMReader;

import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;

/**
 * Test class for class {@link RegRepHelper}
 *
 * @author Philip Helger
 */
public final class RegRepHelperTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (RegRepHelperTest.class);

  private static void _validate (@Nonnull final QueryRequest aQR)
  {
    assertNotNull (aQR);

    if (false)
      LOGGER.info (RegRep4Writer.queryRequest ().setFormattedOutput (true).getAsString (aQR));

    assertNotNull (RegRep4Writer.queryRequest ().getAsDocument (aQR));
  }

  @Test
  public void testRequest ()
  {
    final Document aDoc = DOMReader.readXMLDOM ("<root attr='a' xmlns='urn:anything-weird/bla-foo'><child><child2>value</child2></child></root>");

    final QueryRequest aQR = RegRepHelper.createQueryRequest ("mock-data-request",
                                                              new SlotBuilder ().setName ("IssueDateTime")
                                                                                .setValue (PDTXMLConverter.getXMLCalendarNow ())
                                                                                .build (),
                                                              new SlotBuilder ().setName ("DataConsumerRequestPurpose")
                                                                                .setValue (RegRepHelper.createInternationalStringType (RegRepHelper.createLocalizedString (Locale.ENGLISH,
                                                                                                                                                                           "Qualification Procedure in Public Procurement"),
                                                                                                                                       RegRepHelper.createLocalizedString (Locale.GERMAN,
                                                                                                                                                                           "Qualifizierungsverfahren im öffentlichen Beschaffungswesen")))
                                                                                .build (),
                                                              new SlotBuilder ().setName ("DummyXML")
                                                                                .setValue (aDoc.getDocumentElement ())
                                                                                .build ());
    _validate (aQR);
  }

  private static void _validate (@Nonnull final QueryResponse aQR)
  {
    assertNotNull (aQR);

    if (true)
      LOGGER.info (RegRep4Writer.queryResponse ().setFormattedOutput (true).getAsString (aQR));

    assertNotNull (RegRep4Writer.queryResponse ().getAsDocument (aQR));
  }

  @Test
  public void testResponse ()
  {
    final Document aDoc = DOMReader.readXMLDOM ("<root attr='a' xmlns='urn:anything-weird/bla-foo'><child><child2>value</child2></child></root>");

    final QueryResponse aQR = RegRepHelper.createQueryResponse ("mock-data-Response",
                                                                new SlotBuilder ().setName ("IssueDateTime")
                                                                                  .setValue (PDTXMLConverter.getXMLCalendarNow ())
                                                                                  .build (),
                                                                new SlotBuilder ().setName ("DataConsumerResponsePurpose")
                                                                                  .setValue (RegRepHelper.createInternationalStringType (RegRepHelper.createLocalizedString (Locale.ENGLISH,
                                                                                                                                                                             "Qualification Procedure in Public Procurement"),
                                                                                                                                         RegRepHelper.createLocalizedString (Locale.GERMAN,
                                                                                                                                                                             "Qualifizierungsverfahren im öffentlichen Beschaffungswesen")))
                                                                                  .build (),
                                                                new SlotBuilder ().setName ("DummyXML")
                                                                                  .setValue (aDoc.getDocumentElement ())
                                                                                  .build ());
    _validate (aQR);
  }
}
