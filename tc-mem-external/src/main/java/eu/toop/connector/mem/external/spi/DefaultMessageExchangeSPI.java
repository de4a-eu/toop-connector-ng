/**
 * Copyright (C) 2018-2020 toop.eu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.connector.mem.external.spi;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.IsSPIImplementation;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.EErrorLevel;

import com.helger.peppolid.IDocumentTypeIdentifier;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.IProcessIdentifier;
import com.helger.peppolid.simple.participant.SimpleParticipantIdentifier;
import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.IMessageExchangeSPI;
import eu.toop.connector.api.me.incoming.IMEIncomingHandler;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.incoming.MEIncomingTransportMetadata;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;
import eu.toop.connector.api.me.outgoing.MEOutgoingException;
import eu.toop.connector.mem.external.GatewayRoutingMetadata;
import eu.toop.connector.mem.external.MEMDelegate;
import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;
import eu.toop.edm.xml.EDMPayloadDeterminator;
import eu.toop.kafkaclient.ToopKafkaClient;

/**
 * Implementation of {@link IMessageExchangeSPI} using the "TOOP AS4 Gateway
 * back-end interface".
 *
 * @author Philip Helger
 */
@IsSPIImplementation
public class DefaultMessageExchangeSPI implements IMessageExchangeSPI {
  private IMEIncomingHandler m_aIncomingHandler;

  public DefaultMessageExchangeSPI() {
  }

  @Nonnull
  @Nonempty
  public String getID() {
    return "external";
  }

  public void registerIncomingHandler(@Nonnull final ServletContext aServletContext,
                                      @Nonnull final IMEIncomingHandler aIncomingHandler) {
    ValueEnforcer.notNull(aServletContext, "ServletContext");
    ValueEnforcer.notNull(aIncomingHandler, "IncomingHandler");
    if (m_aIncomingHandler != null)
      throw new IllegalStateException("Another incoming handler was already registered!");
    m_aIncomingHandler = aIncomingHandler;

    final MEMDelegate aDelegate = MEMDelegate.getInstance();

    aDelegate.registerNotificationHandler(aRelayResult -> {
      // more to come
      ToopKafkaClient.send(EErrorLevel.INFO,
          () -> "Notification[" + aRelayResult.getErrorCode() + "]: " + aRelayResult.getDescription());
    });

    aDelegate.registerSubmissionResultHandler(aRelayResult -> {
      // more to come
      ToopKafkaClient.send(EErrorLevel.INFO,
          () -> "SubmissionResult[" + aRelayResult.getErrorCode() +
              "]: " +
              aRelayResult.getDescription());
    });

    // Register the AS4 handler needed
    aDelegate.registerMessageHandler(aMEMessage -> {
      final MEPayload aHead = aMEMessage.payloads().getFirst();
      final IEDMTopLevelObject aTopLevel = EDMPayloadDeterminator.parseAndFind(aHead.getData().getInputStream());
      // TODO get metadata in here


      final IParticipantIdentifier sender = TCConfig.getIdentifierFactory().parseParticipantIdentifier(aMEMessage.getSenderId());
      final IParticipantIdentifier receiver = TCConfig.getIdentifierFactory().parseParticipantIdentifier(aMEMessage.getReceiverId());
      final IDocumentTypeIdentifier docid = TCConfig.getIdentifierFactory().parseDocumentTypeIdentifier(aMEMessage.getDoctypeId());
      final IProcessIdentifier procid = TCConfig.getIdentifierFactory().parseProcessIdentifier(aMEMessage.getProcessId());

      final MEIncomingTransportMetadata aMetadata = new MEIncomingTransportMetadata(sender, receiver, docid, procid);

      if (aTopLevel instanceof EDMRequest) {
        // Request
        m_aIncomingHandler.handleIncomingRequest(new IncomingEDMRequest((EDMRequest) aTopLevel, aMetadata));
      } else if (aTopLevel instanceof EDMResponse) {
        // Response
        final ICommonsList<MEPayload> aAttachments = new CommonsArrayList<>();
        for (final MEPayload aItem : aMEMessage.payloads())
          if (aItem != aHead)
            aAttachments.add(aItem);
        m_aIncomingHandler.handleIncomingResponse(new IncomingEDMResponse((EDMResponse) aTopLevel,
            aAttachments,
            aMetadata));
      } else if (aTopLevel instanceof EDMErrorResponse) {
        // Error response
        m_aIncomingHandler.handleIncomingErrorResponse(new IncomingEDMErrorResponse((EDMErrorResponse) aTopLevel,
            aMetadata));
      } else {
        // Unknown
        ToopKafkaClient.send(EErrorLevel.ERROR, () -> "Unsuspported Message: " + aTopLevel);
      }
    });
  }

  public void sendOutgoing(@Nonnull final IMERoutingInformation aRoutingInfo, @Nonnull final MEMessage aMessage)
      throws MEOutgoingException {
    final GatewayRoutingMetadata aGRM = new GatewayRoutingMetadata(aRoutingInfo.getSenderID().getURIEncoded(),
        aRoutingInfo.getReceiverID().getURIEncoded(),
        aRoutingInfo.getDocumentTypeID().getURIEncoded(),
        aRoutingInfo.getProcessID().getURIEncoded(),
        aRoutingInfo.getEndpointURL(),
        aRoutingInfo.getCertificate());
    MEMDelegate.getInstance().sendMessage(aGRM, aMessage);
  }

  public void shutdown(@Nonnull final ServletContext aServletContext) {
  }
}
