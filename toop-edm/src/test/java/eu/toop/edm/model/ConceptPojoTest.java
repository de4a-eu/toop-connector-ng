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
package eu.toop.edm.model;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.commons.url.URLHelper;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.edm.xml.cccev.ConceptMarshaller;

/**
 * Test class for class {@link ConceptPojo}
 *
 * @author Philip Helger
 */
public final class ConceptPojoTest
{
  private static void _testWriteAndRead (@Nonnull final ConceptPojo x)
  {
    assertNotNull (x);

    final CCCEVConceptType aConcept = x.getAsCCCEVConcept ();
    assertNotNull (aConcept);

    // Write
    final ConceptMarshaller m = new ConceptMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aConcept));

    // Re-read
    final ConceptPojo y = ConceptPojo.builder (aConcept).build ();
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testBasic ()
  {
    final String NS = "http://toop.eu/registered-organization";
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (NS, "CompanyData")
                                     .addChild (ConceptPojo.builder ().id ("ConceptID-2").name (NS, "Concept-Name-2"))
                                     .addChild (ConceptPojo.builder ().id ("ConceptID-3").name (NS, "Concept-Name-3"))
                                     .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testPilotGBM ()
  {
    final String NS = "http://toop.eu/registered-organization";
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (NS, "CompanyData")
                                     .addChild (ConceptPojo.builder ()
                                                           .id ("ConceptID-2")
                                                           .name (EToopConcept.COMPANY_NAME))
                                     .addChild (ConceptPojo.builder ()
                                                           .id ("ConceptID-3")
                                                           .name (EToopConcept.COMPANY_TYPE))
                                     .build ();
    _testWriteAndRead (x);
  }

  private static final AtomicInteger COUNT = new AtomicInteger (1);

  @Nonnull
  private static ConceptPojo.Builder _concept ()
  {
    final String sID = "id" + COUNT.incrementAndGet ();
    return ConceptPojo.builder ().id (sID).name ("urn:bla", sID);
  }

  @Nonnull
  private static ConceptPojo.Builder _concept (final ConceptValuePojo.Builder aValue)
  {
    return _concept ().value (aValue);
  }

  @Test
  public void testValues ()
  {
    final String NS = "http://toop.eu/registered-organization";
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (NS, "CompanyData")
                                     .addChild (_concept (ConceptValuePojo.builder ().identifier ("identifier")))
                                     .addChild (_concept (ConceptValuePojo.builder ().amount (BigDecimal.TEN, "EUR")))
                                     .addChild (_concept (ConceptValuePojo.builder ().code ("code")))
                                     .addChild (_concept (ConceptValuePojo.builder ()
                                                                          .date (PDTXMLConverter.getXMLCalendarDateNow ())))
                                     .addChild (_concept (ConceptValuePojo.builder ().indicator (true)))
                                     .addChild (_concept (ConceptValuePojo.builder ().measure (BigDecimal.ONE, "unit")))
                                     .addChild (_concept (ConceptValuePojo.builder ().numeric (42)))
                                     .addChild (_concept (ConceptValuePojo.builder ().numeric (4.2)))
                                     .addChild (_concept (ConceptValuePojo.builder ()
                                                                          .period (PDTFactory.getCurrentLocalDateTime ()
                                                                                             .minusDays (1),
                                                                                   PDTFactory.getCurrentLocalDateTime ()
                                                                                             .plusDays (1))))
                                     .addChild (_concept (ConceptValuePojo.builder ()
                                                                          .quantity (BigDecimal.ONE.negate (), "qty")))
                                     .addChild (_concept (ConceptValuePojo.builder ().text ("a", "b", "c")))
                                     .addChild (_concept (ConceptValuePojo.builder ()
                                                                          .time (PDTXMLConverter.getXMLCalendarTimeNow ())))
                                     .addChild (_concept (ConceptValuePojo.builder ()
                                                                          .uri (URLHelper.getAsURI ("http://toop.eu"))))
                                     .addChild (_concept (ConceptValuePojo.builder ().errorCode ("who-cares")))

                                     .addChild (_concept ().valueID ("identifier"))
                                     .addChild (_concept ().valueAmount (BigDecimal.TEN, "EUR"))
                                     .addChild (_concept ().valueCode ("code"))
                                     .addChild (_concept ().valueDate (PDTFactory.getCurrentLocalDate ()))
                                     .addChild (_concept ().valueIndicator (true))
                                     .addChild (_concept ().valueMeasure (BigDecimal.ONE, "unit"))
                                     .addChild (_concept ().valueNumeric (42))
                                     .addChild (_concept ().valueNumeric (4.2))
                                     .addChild (_concept ().valuePeriod (PDTFactory.getCurrentLocalDateTime ()
                                                                                   .minusDays (1),
                                                                         PDTFactory.getCurrentLocalDateTime ()
                                                                                   .plusDays (1)))
                                     .addChild (_concept ().valueQuantity (BigDecimal.ONE.negate (), "qty"))
                                     .addChild (_concept ().valueText ("a", "b", "c"))
                                     .addChild (_concept ().valueTime (PDTFactory.getCurrentLocalTime ()))
                                     .addChild (_concept ().valueURI ("http://toop.eu"))
                                     .addChild (_concept ().valueErrorCode (EToopErrorCode.DD_003))
                                     .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testMinimum ()
  {
    final ConceptPojo x = ConceptPojo.builder ().build ();
    _testWriteAndRead (x);
  }
}
