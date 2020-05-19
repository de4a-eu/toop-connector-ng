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
package eu.toop.connector.api.smp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;

/**
 * This class implements the {@link ISMPEndpointProvider} interface using a
 * constant endpoint. This implementation is meant for testing purposes only.
 * Don't use in production.
 *
 * @author Philip Helger
 */
public class SMPEndpointProviderConstant implements ISMPEndpointProvider
{
  private final ISMPEndpoint m_aEndpoint;

  /**
   * Constructor with a single endpoint.
   *
   * @param aEndpoint
   *        The participant ID to return. May be <code>null</code>.
   */
  public SMPEndpointProviderConstant (@Nullable final ISMPEndpoint aEndpoint)
  {
    m_aEndpoint = aEndpoint;
  }

  @Nullable
  public ISMPEndpoint getEndpoint (@Nonnull final String sLogPrefix,
                                    @Nonnull final IParticipantIdentifier aRecipientID,
                                    @Nonnull final IDocumentTypeIdentifier aDocumentTypeID,
                                    @Nonnull final IProcessIdentifier aProcessID,
                                    @Nonnull @Nonempty final String sTransportProfileID,
                                    @Nonnull final ISMPErrorHandler aErrorHandler)
  {
    return m_aEndpoint;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Endpoint", m_aEndpoint).getToString ();
  }
}
