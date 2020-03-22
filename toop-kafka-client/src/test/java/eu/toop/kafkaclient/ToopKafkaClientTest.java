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
package eu.toop.kafkaclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.kafka.common.KafkaException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.error.level.EErrorLevel;

/**
 * Test class for class {@link ToopKafkaClient}.
 *
 * @author Philip Helger
 */
public final class ToopKafkaClientTest
{
  @BeforeClass
  public static void beforeAll ()
  {
    ToopKafkaSettings.setKafkaEnabled (true);
  }

  @AfterClass
  public static void afterAll ()
  {
    // Disable again for other tests
    ToopKafkaSettings.setKafkaEnabled (false);
  }

  @Test
  public void testBasic ()
  {
    try
    {
      // Don't send too many - will take forever if no Kafka server is up and
      // running!
      for (int i = 0; i < 5; ++i)
        ToopKafkaClient.send (EErrorLevel.INFO, "Value" + i);
    }
    catch (final KafkaException ex)
    {
      // lets act as if we are not surprised...
    }
    finally
    {
      ToopKafkaClient.close ();
    }
  }

  @Test
  public void testDefaultProperties ()
  {
    final ICommonsMap <String, String> aProps = ToopKafkaSettings.defaultProperties ();
    assertNotNull (aProps);
    // Ensure mutable map
    aProps.put ("foo", "bar");
    assertEquals ("bar", ToopKafkaSettings.defaultProperties ().get ("foo"));
  }
}
