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

import eu.toop.edm.jaxb.dcatap.DCatAPRelationshipType;
import eu.toop.edm.xml.dcatap.RelationshipMarshaller;

/**
 * Test class for class {@link QualifiedRelationPojo}
 *
 * @author Philip Helger
 */
public final class QualifiedRelationPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (QualifiedRelationPojoTest.class);

  @Test
  public void testBasic ()
  {
    final QualifiedRelationPojo x = QualifiedRelationPojo.builder ()
                                                         .addDescription ("desc1")
                                                         .addDescription ("desc2")
                                                         .title ("Title")
                                                         .addID ("ID1")
                                                         .addID ("ID2")
                                                         .build ();
    final DCatAPRelationshipType aQualifiedRelation = x.getAsRelationship ();
    assertNotNull (aQualifiedRelation);

    final RelationshipMarshaller m = new RelationshipMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aQualifiedRelation));
    LOGGER.info (m.getAsString (aQualifiedRelation));
  }

  @Test
  public void testMinimum ()
  {
    final QualifiedRelationPojo x = QualifiedRelationPojo.builder ().description ("desc").title ("ti").build ();
    assertNotNull (x);

    final DCatAPRelationshipType aQualifiedRelation = x.getAsRelationship ();
    assertNotNull (aQualifiedRelation);

    final RelationshipMarshaller m = new RelationshipMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aQualifiedRelation));
    LOGGER.info (m.getAsString (aQualifiedRelation));
  }
}
