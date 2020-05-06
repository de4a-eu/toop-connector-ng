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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.xml.namespace.MapBasedNamespaceContext;

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
  private static final Logger LOGGER = LoggerFactory.getLogger (ConceptPojoTest.class);

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
    final CCCEVConceptType aConcept = x.getAsCCCEVConcept ();
    assertNotNull (aConcept);

    final ConceptMarshaller m = new ConceptMarshaller (new MapBasedNamespaceContext ().addMapping ("toop", NS));
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aConcept));
    LOGGER.info (m.getAsString (aConcept));
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
    final CCCEVConceptType aConcept = x.getAsCCCEVConcept ();
    assertNotNull (aConcept);

    final ConceptMarshaller m = new ConceptMarshaller (new MapBasedNamespaceContext ().addMapping ("toop", NS));
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aConcept));
    LOGGER.info (m.getAsString (aConcept));
  }

  @Test
  public void testMinimum ()
  {
    final ConceptPojo x = ConceptPojo.builder ().build ();
    assertNotNull (x);

    final CCCEVConceptType aConcept = x.getAsCCCEVConcept ();
    assertNotNull (aConcept);

    final ConceptMarshaller m = new ConceptMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aConcept));
    LOGGER.info (m.getAsString (aConcept));
  }
}
