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

import java.util.Locale;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.error.level.IErrorLevel;
import com.helger.commons.lang.ClassHelper;
import com.helger.commons.log.LogHelper;

/**
 * Global TOOP Kafka Client. It is disabled by default.
 *
 * @author Philip Helger
 */
public final class ToopKafkaClient
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToopKafkaClient.class);

  private ToopKafkaClient ()
  {}

  private static void _sendIfKafkaEnabled (@Nullable final IErrorLevel aErrorLevel, @Nonnull final String sValue)
  {
    String sLogText = sValue;
    if (aErrorLevel != null)
      sLogText = "[" + aErrorLevel.getID ().toUpperCase (Locale.US) + "] " + sLogText;

    if (LOGGER.isDebugEnabled ())
      LOGGER.debug ("Sending to Kafka: '" + sLogText + "'");

    // Send but don't wait for the commit!
    ToopKafkaManager.send ((String) null, sLogText, null);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *        Error level to log the message. May be <code>null</code> to not log
   *        it.
   * @param sValue
   *        Value to send. May not be <code>null</code>.
   * @see ToopKafkaSettings#isKafkaEnabled()
   */
  public static void send (@Nullable final IErrorLevel aErrorLevel, @Nonnull final String sValue)
  {
    if (aErrorLevel != null && ToopKafkaSettings.isLoggingEnabled ())
      LogHelper.log (ToopKafkaClient.class, aErrorLevel, sValue);
    if (ToopKafkaSettings.isKafkaEnabled ())
      _sendIfKafkaEnabled (aErrorLevel, sValue);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *        Error level to log the message. May be <code>null</code> to not log
   *        it.
   * @param aValue
   *        Value supplier to send. Is only evaluated if enabled. May not be
   *        <code>null</code>.
   * @see ToopKafkaSettings#isKafkaEnabled()
   */
  public static void send (@Nullable final IErrorLevel aErrorLevel, @Nonnull final Supplier <String> aValue)
  {
    send (aErrorLevel, aValue, (Throwable) null);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *        Error level to log the message. May be <code>null</code> to not log
   *        it.
   * @param aValue
   *        Value supplier to send. Is only evaluated if enabled. May not be
   *        <code>null</code>.
   * @param t
   *        Exception to be logged. May be <code>null</code>.
   * @see ToopKafkaSettings#isKafkaEnabled()
   */
  public static void send (@Nullable final IErrorLevel aErrorLevel,
                           @Nonnull final Supplier <String> aValue,
                           @Nullable final Throwable t)
  {
    String sValue = null;
    if (aErrorLevel != null && ToopKafkaSettings.isLoggingEnabled ())
    {
      sValue = aValue.get ();
      LogHelper.log (ToopKafkaClient.class, aErrorLevel, sValue, t);
    }
    if (ToopKafkaSettings.isKafkaEnabled ())
    {
      if (sValue == null)
        sValue = aValue.get ();
      if (t != null)
        sValue += " -- " + ClassHelper.getClassLocalName (t.getClass ()) + ": " + t.getMessage ();
      _sendIfKafkaEnabled (aErrorLevel, sValue);
    }
  }

  /**
   * Shutdown at the end. Note: this only does something, if the client is
   * enabled. Do this only once globally on application shutdown.
   *
   * @see ToopKafkaSettings#isKafkaEnabled()
   */
  public static void close ()
  {
    if (ToopKafkaSettings.isKafkaEnabled ())
    {
      ToopKafkaManager.shutdown ();
      if (LOGGER.isInfoEnabled ())
        LOGGER.info ("Successfully shutdown Kafka client");
    }
  }
}
