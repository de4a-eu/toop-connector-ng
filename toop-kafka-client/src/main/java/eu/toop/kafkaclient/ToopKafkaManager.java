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

import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;

/**
 * Global Kafka resource manager. Call shutdown() upon end of application.
 *
 * @author Philip Helger
 */
final class ToopKafkaManager
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToopKafkaManager.class);
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static KafkaProducer <String, String> s_aProducer;
  private static final ICommonsMap <String, String> s_aDefaultProps = new CommonsHashMap <> ();

  static
  {
    // Instead of 16K
    // s_aProps.put ("batch.size", "1");
    // Server URL - MUST be configured
    // s_aProps.put ("bootstrap.servers", "193.10.8.211:7073");
    // Default: 5secs
    s_aDefaultProps.put (ProducerConfig.MAX_BLOCK_MS_CONFIG, "5000");
  }

  /**
   * @return The default properties for customization. Changes to this map only
   *         effect new connections! Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject
  public static ICommonsMap <String, String> defaultProperties ()
  {
    return s_aDefaultProps;
  }

  private ToopKafkaManager ()
  {}

  @Nonnull
  @ReturnsMutableObject
  private static ICommonsMap <String, Object> _getCreationProperties ()
  {
    final ICommonsMap <String, Object> aProps = new CommonsHashMap <> ();
    // Use all default props
    aProps.putAll (s_aDefaultProps);
    return aProps;
  }

  /**
   * Init the global {@link KafkaProducer} - must be called once before the
   * first message is logged. This is only invoked internally.
   *
   * @return The non-<code>null</code> producer to be used.
   * @throws KafkaException
   *         in case of invalid properties (like non-existing server domain)
   */
  @Nonnull
  public static KafkaProducer <String, String> getOrCreateProducer ()
  {
    // Read-lock first
    KafkaProducer <String, String> ret = s_aRWLock.readLockedGet ( () -> s_aProducer);
    if (ret == null)
    {
      s_aRWLock.writeLock ().lock ();
      try
      {
        // Try again in write lock
        ret = s_aProducer;
        if (ret == null)
        {
          // Create new one
          s_aProducer = ret = new KafkaProducer <> (_getCreationProperties (),
                                                    new StringSerializer (),
                                                    new StringSerializer ());
          if (LOGGER.isDebugEnabled ())
            LOGGER.debug ("Successfully created new KafkaProducer");
        }
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return ret;
  }

  /**
   * Shutdown the global {@link KafkaProducer}. This method can be called
   * independent of the initialization state.
   */
  public static void shutdown ()
  {
    s_aRWLock.writeLocked ( () -> {
      if (s_aProducer != null)
      {
        s_aProducer.close ();
        s_aProducer = null;
        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("Successfully closed KafkaProducer");
      }
    });
  }

  /**
   * Main sending of a message. Since the send call is asynchronous it returns a
   * Future for the RecordMetadata that will be assigned to this record.
   * Invoking get() on this future will block until the associated request
   * completes and then return the metadata for the record or throw any
   * exception that occurred while sending the record.
   *
   * @param sKey
   *        Key to be send. May be <code>null</code>.
   * @param sValue
   *        Value to be send. May not be <code>null</code>.
   * @param aKafkaCallback
   *        Optional Kafka callback
   * @return The {@link Future} with the details on message receipt
   */
  @Nonnull
  public static Future <RecordMetadata> send (@Nullable final String sKey,
                                              @Nonnull final String sValue,
                                              @Nullable final Callback aKafkaCallback)
  {
    ValueEnforcer.notNull (sValue, "Value");

    final ProducerRecord <String, String> aMessage = new ProducerRecord <> (ToopKafkaSettings.getKafkaTopic (),
                                                                            sKey,
                                                                            sValue);
    return getOrCreateProducer ().send (aMessage, aKafkaCallback);
  }
}
