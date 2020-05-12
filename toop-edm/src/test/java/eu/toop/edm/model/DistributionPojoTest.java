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

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.mime.CMimeType;
import com.helger.commons.mock.CommonsTestHelper;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.xml.dcatap.DistributionMarshaller;

/**
 * Test class for class {@link DistributionPojo}
 *
 * @author Philip Helger
 */
public final class DistributionPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DistributionPojoTest.class);

  private static void _testWriteAndRead (@Nonnull final DistributionPojo x)
  {
    assertNotNull (x);

    final DCatAPDistributionType aDist = x.getAsDistribution ();
    assertNotNull (aDist);

    // Write
    final DistributionMarshaller m = new DistributionMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aDist));
    LOGGER.info (m.getAsString (aDist));

    // Re-read
    final DistributionPojo y = DistributionPojo.builder (aDist).build ();
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testBasic ()
  {
    final DistributionPojo x = DistributionPojo.builder ()
                                               .format (EDistributionFormat.STRUCTURED)
                                               .mediaType (CMimeType.TEXT_PLAIN)
                                               .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testMinimum ()
  {
    final DistributionPojo x = DistributionPojo.builder ().build ();
    _testWriteAndRead (x);
  }
}
