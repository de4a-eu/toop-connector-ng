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
package eu.toop.connector.app.api;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.bdve.api.executorset.VESID;
import com.helger.bdve.api.result.ValidationResultList;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.collection.impl.ICommonsSortedMap;
import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.xsds.bdxr.smp1.EndpointType;
import com.helger.xsds.bdxr.smp1.ServiceMetadataType;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.ITCErrorHandler;
import eu.toop.connector.api.error.LoggingTCErrorHandler;
import eu.toop.connector.api.me.IMessageExchangeSPI;
import eu.toop.connector.api.me.MessageExchangeManager;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MEOutgoingException;

/**
 * A utility class that provides abstractions for all major tasks to be invoked
 * by the TC and is also used from the REST API components.
 *
 * @author Philip Helger
 * @since 2.0.0-rc4
 */
@Immutable
public final class TCAPIHelper
{
  public static final Locale DEFAULT_LOCALE = Locale.US;

  private TCAPIHelper ()
  {}

  /**
   * @param sDatasetType
   *        Dataset Type to query. May not be <code>null</code>.
   * @param sCountryCode
   *        Country code to use. Must be a 2-digit string. May be
   *        <code>null</code>.
   * @param aErrorHdl
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of datasets.
   */
  public static ICommonsSet <DSDDatasetResponse> getDSDDatasets (@Nonnull final String sDatasetType,
                                                                 @Nullable final String sCountryCode,
                                                                 @Nonnull final ITCErrorHandler aErrorHdl)
  {
    return TCAPIConfig.getDSDDatasetResponseProvider ()
                      .getAllDatasetResponses ("[dsd-by-country]", sDatasetType, sCountryCode, aErrorHdl);
  }

  /**
   * @param aParticipantID
   *        Participant ID to query.
   * @return A non-<code>null</code> sorted map of all hrefs. The key MUST be
   *         URL decoded whereas the value is the "original href" as found in
   *         the response.
   */
  @Nonnull
  public static ICommonsSortedMap <String, String> querySMPServiceGroups (final IParticipantIdentifier aParticipantID)
  {
    return TCAPIConfig.getDDServiceGroupHrefProvider ()
                      .getAllServiceGroupHrefs (aParticipantID, LoggingTCErrorHandler.INSTANCE);
  }

  /**
   * Find the service metadata. This returns all endponts for the combination.
   *
   * @param aParticipantID
   *        Participant ID to query. May not be <code>null</code>.
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @return <code>null</code> if not found.
   */
  @Nullable
  public static ServiceMetadataType querySMPServiceMetadata (@Nonnull final IParticipantIdentifier aParticipantID,
                                                             @Nonnull final IDocumentTypeIdentifier aDocTypeID)
  {
    return TCAPIConfig.getDDServiceMetadataProvider ().getServiceMetadata (aParticipantID, aDocTypeID);
  }

  /**
   * Find the dynamic discovery endpoint from the respective parameters. This
   * calls
   * {@link #querySMPServiceMetadata(IParticipantIdentifier, IDocumentTypeIdentifier)}
   * and filters out the matching process ID and transport profile ID.
   *
   * @param aParticipantID
   *        Participant ID to query. May not be <code>null</code>.
   * @param aDocTypeID
   *        Document type ID. May not be <code>null</code>.
   * @param aProcessID
   *        Process ID. May not be <code>null</code>.
   * @param sTransportProfile
   *        Transport profile to be used. May not be <code>null</code>.
   * @return <code>null</code> if no such endpoint was found
   */
  @Nullable
  public static EndpointType querySMPEndpoint (@Nonnull final IParticipantIdentifier aParticipantID,
                                               @Nonnull final IDocumentTypeIdentifier aDocTypeID,
                                               @Nonnull final IProcessIdentifier aProcessID,
                                               @Nonnull final String sTransportProfile)
  {
    return TCAPIConfig.getDDServiceMetadataProvider ()
                      .getEndpoint (aParticipantID, aDocTypeID, aProcessID, sTransportProfile);
  }

  /**
   * Perform validation
   *
   * @param aVESID
   *        VESID to use.
   * @param aPayload
   *        Payload to validate.
   * @return A non-<code>null</code> result list.
   */
  @Nonnull
  public static ValidationResultList validateBusinessDocument (@Nonnull final VESID aVESID,
                                                               @Nonnull final byte [] aPayload)
  {
    return TCAPIConfig.getVSValidator ().validate (aVESID, aPayload, DEFAULT_LOCALE);
  }

  /**
   * Send an AS4 message using the configured Message Exchange Module (MEM).
   *
   * @param aRoutingInfo
   *        Routing information. May not be <code>null</code>.
   * @param aMessage
   *        The message to be exchanged. May not be <code>null</code>.
   * @throws MEOutgoingException
   *         In case of error.
   */
  public static void sendAS4Message (@Nonnull final IMERoutingInformation aRoutingInfo,
                                     @Nonnull final MEMessage aMessage) throws MEOutgoingException
  {
    final IMessageExchangeSPI aMEM = MessageExchangeManager.getConfiguredImplementation ();
    aMEM.sendOutgoing (aRoutingInfo, aMessage);
  }
}
