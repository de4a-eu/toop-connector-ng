package eu.toop.connector.app.incoming;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.mime.IMimeType;
import com.helger.peppolid.IIdentifier;

import eu.toop.connector.api.me.incoming.IMEIncomingTransportMetadata;
import eu.toop.connector.api.me.incoming.IncomingEDMErrorResponse;
import eu.toop.connector.api.me.incoming.IncomingEDMRequest;
import eu.toop.connector.api.me.incoming.IncomingEDMResponse;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.rest.TCIdentifierType;
import eu.toop.connector.api.rest.TCIncomingMessage;
import eu.toop.connector.api.rest.TCIncomingMetadata;
import eu.toop.connector.api.rest.TCPayload;
import eu.toop.connector.api.rest.TCPayloadType;

/**
 * Push incoming messages to DC/DP
 *
 * @author Philip Helger
 */
public final class MPTrigger
{
  private MPTrigger ()
  {}

  private static void _forwardMessage (@Nonnull final TCIncomingMessage aMsg)
  {
    // TODO
  }

  @Nonnull
  private static TCIdentifierType _createID (@Nonnull final IIdentifier aID)
  {
    final TCIdentifierType ret = new TCIdentifierType ();
    ret.setScheme (aID.getScheme ());
    ret.setValue (aID.getValue ());
    return ret;
  }

  @Nonnull
  private static TCIncomingMetadata _createMetadata (@Nonnull final IMEIncomingTransportMetadata aMD,
                                                     @Nonnull final TCPayloadType ePayloadType)
  {
    final TCIncomingMetadata ret = new TCIncomingMetadata ();
    ret.setSenderID (_createID (aMD.getSenderID ()));
    ret.setReceiverID (_createID (aMD.getReceiverID ()));
    ret.setDocTypeID (_createID (aMD.getDocumentTypeID ()));
    ret.setProcessID (_createID (aMD.getProcessID ()));
    ret.setPayloadType (ePayloadType);
    return ret;
  }

  @Nonnull
  private static TCPayload _createPayload (@Nonnull final byte [] aValue,
                                           @Nullable final String sContentID,
                                           @Nonnull final IMimeType aMimeType)
  {
    final TCPayload ret = new TCPayload ();
    ret.setValue (aValue);
    ret.setContentID (sContentID);
    ret.setMimeType (aMimeType.getAsString ());
    return ret;
  }

  public static void forwardMessage (@Nonnull final IncomingEDMRequest aRequest)
  {
    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aRequest.getMetadata (), TCPayloadType.REQUEST));
    aMsg.addPayload (_createPayload (aRequest.getRequest ().getWriter ().getAsBytes (), null, MEPayload.MIME_TYPE_REG_REP));
    _forwardMessage (aMsg);
  }

  public static void forwardMessage (@Nonnull final IncomingEDMResponse aResponse)
  {
    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aResponse.getMetadata (), TCPayloadType.RESPONSE));
    aMsg.addPayload (_createPayload (aResponse.getResponse ().getWriter ().getAsBytes (), null, MEPayload.MIME_TYPE_REG_REP));
    // Add all attachments
    for (final MEPayload aPayload : aResponse.attachments ().values ())
      aMsg.addPayload (_createPayload (aPayload.getData ().bytes (), aPayload.getContentID (), aPayload.getMimeType ()));
    _forwardMessage (aMsg);
  }

  public static void forwardMessage (@Nonnull final IncomingEDMErrorResponse aErrorResponse)
  {
    final TCIncomingMessage aMsg = new TCIncomingMessage ();
    aMsg.setMetadata (_createMetadata (aErrorResponse.getMetadata (), TCPayloadType.ERROR_RESPONSE));
    aMsg.addPayload (_createPayload (aErrorResponse.getErrorResponse ().getWriter ().getAsBytes (), null, MEPayload.MIME_TYPE_REG_REP));
    _forwardMessage (aMsg);
  }
}
