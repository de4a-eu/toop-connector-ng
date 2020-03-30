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
package eu.toop.edm.xhe;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.peppolid.simple.participant.SimpleParticipantIdentifier;
import com.helger.xhe.XHE10Marshaller;
import com.helger.xhe.v10.XHE10XHEType;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;

/**
 * Test class for class {@link TOOPXHEBuilder}.
 *
 * @author Philip Helger
 */
public final class TOOPXHEBuilderTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPXHEBuilderTest.class);

  private static void _validate (@Nonnull final XHE10XHEType aXHE)
  {
    assertNotNull (aXHE);

    if (true)
    {
      final XHE10Marshaller x = new XHE10Marshaller (false);
      x.setFormattedOutput (true);
      LOGGER.info (x.getAsString (aXHE));
    }

    final XHE10Marshaller aMarshaller = new XHE10Marshaller (true);
    assertNotNull (aMarshaller.getAsDocument (aXHE));
  }

  @Test
  public void testBasic ()
  {
    final XHE10XHEType aXHE = new TOOPXHEBuilder ().addParameter ("DCCountry", "AT")
                                                   .setFromParty (new SimpleParticipantIdentifier ("scheme", "from"))
                                                   .setToParty (new SimpleParticipantIdentifier ("scheme", "to1"))
                                                   .setDocumentTypeID (EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION)
                                                   .setProcessID (EPredefinedProcessIdentifier.DATAREQUESTRESPONSE)
                                                   .setPayloadType (EXHEPayloadType.DATA_REQUEST)
                                                   .build ();
    _validate (aXHE);
  }
}
