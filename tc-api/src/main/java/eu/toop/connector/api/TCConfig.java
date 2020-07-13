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
package eu.toop.connector.api;

import java.net.URI;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.id.factory.GlobalIDFactory;
import com.helger.commons.url.URLHelper;
import com.helger.config.Config;
import com.helger.config.ConfigFactory;
import com.helger.config.IConfig;
import com.helger.config.source.MultiConfigurationValueProvider;
import com.helger.peppol.sml.ESML;
import com.helger.peppol.sml.ISMLInfo;
import com.helger.peppol.sml.SMLInfo;
import com.helger.peppolid.factory.IIdentifierFactory;

import eu.toop.connector.api.me.EMEProtocol;

/**
 * This class contains global configuration elements for the TOOP Connector.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class TCConfig
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static IConfig s_aConfig;

  static
  {
    setDefaultConfig ();
  }

  private TCConfig ()
  {}

  /**
   * @return The configuration file. Never <code>null</code>.
   */
  @Nonnull
  public static IConfig getConfig ()
  {
    return s_aRWLock.readLockedGet ( () -> s_aConfig);
  }

  /**
   * Set a different configuration. E.g. for testing.
   *
   * @param aConfig
   *        The config to be set. May not be <code>null</code>.
   */
  public static void setConfig (@Nonnull final IConfig aConfig)
  {
    ValueEnforcer.notNull (aConfig, "Config");
    s_aRWLock.writeLockedGet ( () -> s_aConfig = aConfig);
  }

  /**
   * Set the default configuration.
   */
  public static void setDefaultConfig ()
  {
    final MultiConfigurationValueProvider aMCSVP = ConfigFactory.createDefaultValueProvider ();
    final IConfig aConfig = Config.create (aMCSVP);
    setConfig (aConfig);
  }

  @Nonnull
  public static IIdentifierFactory getIdentifierFactory ()
  {
    return TCIdentifierFactory.INSTANCE_TC;
  }

  public static class Global
  {
    private Global ()
    {}

    public static boolean isGlobalDebug ()
    {
      return getConfig ().getAsBoolean ("global.debug", GlobalDebug.isDebugMode ());
    }

    public static boolean isGlobalProduction ()
    {
      return getConfig ().getAsBoolean ("global.production", GlobalDebug.isProductionMode ());
    }

    /**
     * @return A debug name to identify an instance, especially in the tracker.
     */
    @Nullable
    public static String getToopInstanceName ()
    {
      return getConfig ().getAsString ("global.instancename");
    }
  }

  public static class Tracker
  {
    public static final boolean DEFAULT_TOOP_TRACKER_ENABLED = false;
    public static final String DEFAULT_TOOP_TRACKER_TOPIC = "toop";

    private Tracker ()
    {}

    public static boolean isToopTrackerEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.tracker.enabled", DEFAULT_TOOP_TRACKER_ENABLED);
    }

    @Nullable
    public static String getToopTrackerUrl ()
    {
      return getConfig ().getAsString ("toop.tracker.url");
    }

    @Nullable
    public static String getToopTrackerTopic ()
    {
      return getConfig ().getAsString ("toop.tracker.topic", DEFAULT_TOOP_TRACKER_TOPIC);
    }
  }

  public static class DSD
  {
    private DSD ()
    {}

    /**
     * @return The DSD base URL. Should never end with a slash.
     */
    @Nullable
    public static String getDSDBaseUrl ()
    {
      return getConfig ().getAsString ("toop.dsd.service.baseurl");
    }
  }

  public static class R2D2
  {
    public static final boolean DEFAULT_USE_SML = true;
    private static ISMLInfo s_aCachedSMLInfo;

    private R2D2 ()
    {}

    /**
     * @return <code>true</code> to use SML lookup, <code>false</code> to not do
     *         it.
     * @see #getR2D2SML()
     * @see #getR2D2SMPUrl()
     */
    public static boolean isR2D2UseDNS ()
    {
      return getConfig ().getAsBoolean ("toop.r2d2.usedns", DEFAULT_USE_SML);
    }

    /**
     * @return The SML URL to be used. Must only contain a value if
     *         {@link #isR2D2UseDNS()} returned <code>true</code>.
     */
    @Nonnull
    public static ISMLInfo getR2D2SML ()
    {
      ISMLInfo ret = s_aCachedSMLInfo;
      if (ret == null)
      {
        final String sSMLID = getConfig ().getAsString ("toop.r2d2.sml.id");
        final ESML eSML = ESML.getFromIDOrNull (sSMLID);
        if (eSML != null)
        {
          // Pre-configured SML it is
          ret = eSML;
        }
        else
        {
          // Custom SML
          final String sDisplayName = getConfig ().getAsString ("toop.r2d2.sml.name", "TOOP SML");
          // E.g. edelivery.tech.ec.europa.eu.
          final String sDNSZone = getConfig ().getAsString ("toop.r2d2.sml.dnszone");
          // E.g. https://edelivery.tech.ec.europa.eu/edelivery-sml
          final String sManagementServiceURL = getConfig ().getAsString ("toop.r2d2.sml.serviceurl");
          final boolean bClientCertificateRequired = getConfig ().getAsBoolean ("toop.r2d2.sml.clientcert", false);
          // No need for a persistent ID here
          ret = new SMLInfo (GlobalIDFactory.getNewStringID (), sDisplayName, sDNSZone, sManagementServiceURL, bClientCertificateRequired);
        }
        // Remember in cache
        s_aCachedSMLInfo = ret;
      }
      return ret;
    }

    /**
     * @return The constant SMP URI to be used. Must only contain a value if
     *         {@link #isR2D2UseDNS()} returned <code>false</code>.
     */
    @Nullable
    public static URI getR2D2SMPUrl ()
    {
      // E.g. http://smp.central.toop
      final String sURI = getConfig ().getAsString ("toop.r2d2.smp.url");
      return URLHelper.getAsURI (sURI);
    }
  }

  public static class MEM
  {
    private MEM ()
    {}

    /**
     * @return The MEM implementation ID or the default value. Never
     *         <code>null</code>.
     */
    @Nullable
    public static String getMEMImplementationID ()
    {
      return getConfig ().getAsString ("toop.mem.implementation");
    }

    /**
     * Get the overall protocol to be used. Depending on that output different
     * other properties might be queried.
     *
     * @return The overall protocol to use. Never <code>null</code>.
     */
    @Nonnull
    public static EMEProtocol getMEMProtocol ()
    {
      final String sID = getConfig ().getAsString ("toop.mem.protocol", EMEProtocol.DEFAULT.getID ());
      final EMEProtocol eProtocol = EMEProtocol.getFromIDOrNull (sID);
      if (eProtocol == null)
        throw new IllegalStateException ("Failed to resolve protocol with ID '" + sID + "'");
      return eProtocol;
    }

    // GW_URL
    @Nullable
    public static String getMEMAS4Endpoint ()
    {
      return getConfig ().getAsString ("toop.mem.as4.endpoint");
    }

    @Nullable
    public static String getMEMAS4GwPartyID ()
    {
      return getConfig ().getAsString ("toop.mem.as4.gw.partyid");
    }

    @Nullable
    public static String getMEMAS4TcPartyid ()
    {
      return getConfig ().getAsString ("toop.mem.as4.tc.partyid");
    }

    public static long getGatewayNotificationWaitTimeout ()
    {
      return getConfig ().getAsLong ("toop.mem.as4.notificationWaitTimeout", 20000);
    }

    /**
     * @return The DSC/DP URL where incoming AS4 messages are forwarded to. This
     *         is the value from the configuration file.
     */
    @Nullable
    public static String getMEMIncomingURL ()
    {
      return getConfig ().getAsString ("toop.mem.incoming.url");
    }

    public static boolean isMEMOutgoingDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.mem.outgoing.dump.enabled", false);
    }

    @Nullable
    public static String getMEMOutgoingDumpPath ()
    {
      return getConfig ().getAsString ("toop.mem.outgoing.dump.path");
    }

    public static boolean isMEMIncomingDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.mem.incoming.dump.enabled", false);
    }

    @Nullable
    public static String getMEMIncomingDumpPath ()
    {
      return getConfig ().getAsString ("toop.mem.incoming.dump.path");
    }
  }

  public static class HTTP
  {
    private HTTP ()
    {}

    public static boolean isProxyServerEnabled ()
    {
      return getConfig ().getAsBoolean ("http.proxy.enabled", false);
    }

    @Nullable
    public static String getProxyServerAddress ()
    {
      // Scheme plus hostname or IP address
      return getConfig ().getAsString ("http.proxy.address");
    }

    @CheckForSigned
    public static int getProxyServerPort ()
    {
      return getConfig ().getAsInt ("http.proxy.port", -1);
    }

    @Nullable
    public static String getProxyServerNonProxyHosts ()
    {
      // Separated by pipe
      return getConfig ().getAsString ("http.proxy.non-proxy");
    }

    public static boolean isTLSTrustAll ()
    {
      return getConfig ().getAsBoolean ("http.tls.trustall", false);
    }

    public static int getConnectionTimeoutMS ()
    {
      return getConfig ().getAsInt ("http.connection-timeout", -1);
    }

    public static int getReadTimeoutMS ()
    {
      return getConfig ().getAsInt ("http.read-timeout", -1);
    }
  }

  public static class WebApp
  {
    private WebApp ()
    {}

    public static boolean isStatusEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.webapp.status.enabled", true);
    }
  }
}
