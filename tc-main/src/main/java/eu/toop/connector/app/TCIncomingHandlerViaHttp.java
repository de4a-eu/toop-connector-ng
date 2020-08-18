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
package eu.toop.connector.app;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.error.level.EErrorLevel;

import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.incoming.MEIncomingException;
import eu.toop.connector.app.incoming.DC_DP_TriggerViaHttp;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Implementation of {@link IMEIncomingHandler} using
 * {@link DC_DP_TriggerViaHttp} to forward the message. By default this class is
 * invoked if an incoming AS4 message is received.
 *
 * @author Philip Helger
 * @since 2.0.0-rc4
 */
public class TCIncomingHandlerViaHttp implements IMEIncomingHandler
{
  private final String m_sLogPrefix;

  /**
   * @param sLogPrefix
   *        The log prefix to use. May not be <code>null</code> but maybe empty.
   */
  public TCIncomingHandlerViaHttp (@Nonnull final String sLogPrefix)
  {
    m_sLogPrefix = ValueEnforcer.notNull (sLogPrefix, "LogPrefix");
  }

  public void handleIncomingRequest (@Nonnull final IncomingEDMRequest aRequest) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO, () -> m_sLogPrefix + "TC got DP incoming MEM request (2/4)");
    DC_DP_TriggerViaHttp.forwardMessage (aRequest);
  }

  public void handleIncomingResponse (@Nonnull final IncomingEDMResponse aResponse) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO,
                          () -> m_sLogPrefix +
                                "TC got DC incoming MEM response (4/4) with " +
                                aResponse.attachments ().size () +
                                " attachments");
    DC_DP_TriggerViaHttp.forwardMessage (aResponse);
  }

  public void handleIncomingErrorResponse (@Nonnull final IncomingEDMErrorResponse aErrorResponse) throws MEIncomingException
  {
    ToopKafkaClient.send (EErrorLevel.INFO, () -> m_sLogPrefix + "TC got DC incoming MEM response (4/4) with ERRORs");
    DC_DP_TriggerViaHttp.forwardMessage (aErrorResponse);
  }
}
