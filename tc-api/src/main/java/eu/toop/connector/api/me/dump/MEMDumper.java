package eu.toop.connector.api.me.dump;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.base64.Base64;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTIOHelper;
import com.helger.json.IJsonArray;
import com.helger.json.IJsonObject;
import com.helger.json.JsonArray;
import com.helger.json.JsonObject;
import com.helger.json.serialize.JsonWriter;
import com.helger.json.serialize.JsonWriterSettings;
import com.helger.security.certificate.CertificateHelper;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.me.model.MEMessage;
import eu.toop.connector.api.me.model.MEPayload;
import eu.toop.connector.api.me.outgoing.IMERoutingInformation;

@Immutable
public final class MEMDumper
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MEMDumper.class);

  private MEMDumper ()
  {}

  /**
   * Dump an outgoing message if dumping is enabled.
   *
   * @param aRoutingInfo
   *        Routing information. May not be <code>null</code>.
   * @param aMessage
   *        The message to be exchanged. May not be <code>null</code>.
   */
  public static void dumpOutgoingMessage (@Nonnull final IMERoutingInformation aRoutingInfo, @Nonnull final MEMessage aMessage)
  {
    if (TCConfig.MEM.isMEMOutgoingDumpEnabled ())
    {
      final String sPath = TCConfig.MEM.getMEMOutgoingDumpPath ();
      if (StringHelper.hasText (sPath))
      {
        final IJsonObject aJson = new JsonObject ().addJson ("routing",
                                                             new JsonObject ().add ("sender", aRoutingInfo.getSenderID ().getURIEncoded ())
                                                                              .add ("receiver",
                                                                                    aRoutingInfo.getReceiverID ().getURIEncoded ())
                                                                              .add ("doctype",
                                                                                    aRoutingInfo.getDocumentTypeID ().getURIEncoded ())
                                                                              .add ("process",
                                                                                    aRoutingInfo.getProcessID ().getURIEncoded ())
                                                                              .add ("transportProtocol",
                                                                                    aRoutingInfo.getTransportProtocol ())

                                                                              .add ("endpointURL", aRoutingInfo.getEndpointURL ())
                                                                              .add ("certificate",
                                                                                    CertificateHelper.getPEMEncodedCertificate (aRoutingInfo.getCertificate ())));
        final IJsonObject aJsonMessage = new JsonObject ();
        if (aMessage.getSenderID () != null)
          aJsonMessage.add ("sender", aMessage.getSenderID ().getURIEncoded ());
        if (aMessage.getReceiverID () != null)
          aJsonMessage.add ("receiver", aMessage.getReceiverID ().getURIEncoded ());
        if (aMessage.getDoctypeID () != null)
          aJsonMessage.add ("doctype", aMessage.getDoctypeID ().getURIEncoded ());
        if (aMessage.getProcessID () != null)
          aJsonMessage.add ("process", aMessage.getProcessID ().getURIEncoded ());
        final IJsonArray aJsonPayloads = new JsonArray ();
        for (final MEPayload aPayload : aMessage.payloads ())
        {
          aJsonPayloads.add (new JsonObject ().add ("mimeType", aPayload.getMimeTypeString ())
                                              .add ("contentID", aPayload.getContentID ())
                                              .add ("data", Base64.safeEncodeBytes (aPayload.getData ().bytes ())));
        }
        aJsonMessage.addJson ("payloads", aJsonPayloads);
        aJson.addJson ("message", aJsonMessage);

        final String sFilename = "toop-outgoing-" + PDTIOHelper.getCurrentLocalDateTimeForFilename () + ".json";
        final File aTargetFile = new File (sPath, sFilename);
        try (final OutputStream aOS = FileHelper.getBufferedOutputStream (aTargetFile))
        {
          new JsonWriter (new JsonWriterSettings ().setIndentEnabled (true)).writeToStream (aJson, aOS, StandardCharsets.UTF_8);
          LOGGER.error ("Wrote outgoing MEM dump file '" + aTargetFile.getAbsolutePath () + "'");
        }
        catch (final IOException ex)
        {
          LOGGER.error ("Error writing to MEM dump file '" + aTargetFile.getAbsolutePath () + "'", ex);
        }
      }
      else
        LOGGER.warn ("Dumping of outgoing MEM messages is enabled, but no dump path was configured. Not dumping the message.");
    }
  }
}
