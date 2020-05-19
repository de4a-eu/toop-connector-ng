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
package eu.toop.connector.api.me.incoming;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.EDMRequest;

/**
 * Incoming EDM request. Uses {@link EDMRequest} and
 * {@link IMEIncomingTransportMetadata} for the metadata.
 *
 * @author Philip Helger
 */
public class IncomingEDMRequest implements IIncomingEDMObject
{
  private final EDMRequest m_aRequest;
  private final IMEIncomingTransportMetadata m_aMetadata;

  public IncomingEDMRequest (@Nonnull final EDMRequest aRequest, @Nonnull final IMEIncomingTransportMetadata aMetadata)
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (aMetadata, "Metadata");
    m_aRequest = aRequest;
    m_aMetadata = aMetadata;
  }

  @Nonnull
  public EDMRequest getRequest ()
  {
    return m_aRequest;
  }

  @Nonnull
  public IMEIncomingTransportMetadata getMetadata ()
  {
    return m_aMetadata;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Request", m_aRequest).append ("Metadata", m_aMetadata).getToString ();
  }
}
