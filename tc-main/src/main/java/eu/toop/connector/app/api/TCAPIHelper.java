package eu.toop.connector.app.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.collection.impl.ICommonsSet;

import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.error.ITCErrorHandler;
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
  public static ICommonsSet <DSDDatasetResponse> getAllDatasets (@Nonnull final String sDatasetType,
                                                                 @Nullable final String sCountryCode,
                                                                 @Nonnull final ITCErrorHandler aErrorHdl)
  {
    return TCAPIConfig.getDSDDatasetResponseProvider ()
                      .getAllDatasetResponses ("[dsd-by-country]", sDatasetType, sCountryCode, aErrorHdl);
  }

  public static void sendAS4Message (@Nonnull final IMERoutingInformation aRoutingInfo,
                                     @Nonnull final MEMessage aMessage) throws MEOutgoingException
  {
    final IMessageExchangeSPI aMEM = MessageExchangeManager.getConfiguredImplementation ();
    aMEM.sendOutgoing (aRoutingInfo, aMessage);
  }
}
