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

import java.io.File;
import java.net.URI;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.id.factory.GlobalIDFactory;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.URLHelper;
import com.helger.config.ConfigFactory;
import com.helger.config.IConfig;
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
  private static final Logger LOGGER = LoggerFactory.getLogger (TCConfig.class);
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
    setConfig (ConfigFactory.getDefaultConfig ());
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
     * @return A debug name to identify an instance. If none is provided, the IP
     *         address is used.
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
     * @return The TOOP Directory base URL for R2D2. Should never end with a
     *         slash.
     */
    @Nullable
    public static String getR2D2DirectoryBaseUrl ()
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

    public static String getMEMAS4TcPartyid ()
    {
      return getConfig ().getAsString ("toop.mem.as4.tc.partyid");
    }

    public static long getGatewayNotificationWaitTimeout ()
    {
      return getConfig ().getAsLong ("toop.mem.as4.notificationWaitTimeout", 20000);
    }
  }

  public static class MP
  {
    @GuardedBy ("s_aRWLock")
    private static String s_sMPToopInterfaceDPOverrideUrl;
    @GuardedBy ("s_aRWLock")
    private static String s_sMPToopInterfaceDCOverrideUrl;

    private MP ()
    {}

    /**
     * @return <code>true</code> if Schematron validation is enabled,
     *         <code>false</code> if not. Default is true.
     */
    public static boolean isMPSchematronValidationEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.mp.schematron.enabled", true);
    }

    /**
     * Override the toop-interface DP URL with the custom URL. This URL has
     * precedence over the value in the configuration file.
     *
     * @param sMPToopInterfaceDPOverrideUrl
     *        The new override URL to set. May be <code>null</code>.
     */
    public static void setMPToopInterfaceDPOverrideUrl (@Nullable final String sMPToopInterfaceDPOverrideUrl)
    {
      if (LOGGER.isWarnEnabled ())
        LOGGER.warn ("Overriding the MP Toop Interface DP URL with '" + sMPToopInterfaceDPOverrideUrl + "'");
      s_aRWLock.writeLockedGet ( () -> s_sMPToopInterfaceDPOverrideUrl = sMPToopInterfaceDPOverrideUrl);
    }

    /**
     * @return The override URL for the toop-interface DP side. May be
     *         <code>null</code>. Default is <code>null</code>.
     */
    @Nullable
    public static String getMPToopInterfaceDPOverrideUrl ()
    {
      return s_aRWLock.readLockedGet ( () -> s_sMPToopInterfaceDPOverrideUrl);
    }

    /**
     * @return The URL of the DP backend for steps 2/4 and 3/4. May be
     *         <code>null</code>.
     * @see #getMPToopInterfaceDPOverrideUrl()
     * @see #setMPToopInterfaceDPOverrideUrl(String)
     */
    @Nullable
    public static String getMPToopInterfaceDPUrl ()
    {
      String ret = getMPToopInterfaceDPOverrideUrl ();
      if (StringHelper.hasNoText (ret))
        ret = getConfig ().getAsString ("toop.mp.dp.url");
      return ret;
    }

    /**
     * Override the toop-interface DC URL with the custom URL. This URL has
     * precedence over the value in the configuration file.
     *
     * @param sMPToopInterfaceDCOverrideUrl
     *        The new override URL to set. May be <code>null</code>.
     */
    public static void setMPToopInterfaceDCOverrideUrl (@Nullable final String sMPToopInterfaceDCOverrideUrl)
    {
      if (LOGGER.isWarnEnabled ())
        LOGGER.warn ("Overriding the MP Toop Interface DC URL with '" + sMPToopInterfaceDCOverrideUrl + "'");
      s_aRWLock.writeLockedGet ( () -> s_sMPToopInterfaceDCOverrideUrl = sMPToopInterfaceDCOverrideUrl);
    }

    /**
     * @return The override URL for the toop-interface DC side. May be
     *         <code>null</code>. Default is <code>null</code>.
     */
    @Nullable
    public static String getMPToopInterfaceDCOverrideUrl ()
    {
      return s_aRWLock.readLockedGet ( () -> s_sMPToopInterfaceDCOverrideUrl);
    }

    /**
     * @return The URL of the DC backend for step 4/4. May be <code>null</code>.
     * @see #getMPToopInterfaceDCOverrideUrl()
     * @see #setMPToopInterfaceDCOverrideUrl(String)
     */
    @Nullable
    public static String getMPToopInterfaceDCUrl ()
    {
      String ret = getMPToopInterfaceDCOverrideUrl ();
      if (StringHelper.hasNoText (ret))
        ret = getConfig ().getAsString ("toop.mp.dc.url");
      return ret;
    }

    /**
     * @return The value of automatically created responses element
     *         <code>RoutingInformation/DataProviderElectronicAddressIdentifier</code>
     */
    @Nonnull
    public static String getMPAutoResponseDPAddressID ()
    {
      return getConfig ().getAsString ("toop.mp.autoresponse.dpaddressid", "error@toop-connector.toop.eu");
    }

    /**
     * @return The ID scheme for the DP in case of automatic responses
     */
    @Nonnull
    public static String getMPAutoResponseDPIDScheme ()
    {
      return getConfig ().getAsString ("toop.mp.autoresponse.dpidscheme");
    }

    /**
     * @return The ID value for the DP in case of automatic responses
     */
    @Nonnull
    public static String getMPAutoResponseDPIDValue ()
    {
      return getConfig ().getAsString ("toop.mp.autoresponse.dpidvalue");
    }

    /**
     * @return The name for the DP in case of automatic responses
     */
    @Nonnull
    public static String getMPAutoResponseDPName ()
    {
      return getConfig ().getAsString ("toop.mp.autoresponse.dpname", "Error@ToopConnector");
    }
  }

  public static class Debug
  {
    private Debug ()
    {}

    // Servlet "/from-dc", step 1/4:

    public static boolean isDebugFromDCDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.debug.from-dc.dump.enabled", false);
    }

    @Nullable
    public static File getDebugFromDCDumpPath ()
    {
      final String sPath = getConfig ().getAsString ("toop.debug.from-dc.dump.path");
      return sPath == null ? null : new File (sPath);
    }

    @Nullable
    public static File getDebugFromDCDumpPathIfEnabled ()
    {
      return isDebugFromDCDumpEnabled () ? getDebugFromDCDumpPath () : null;
    }

    // Servlet "/from-dp", step 3/4:

    public static boolean isDebugFromDPDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.debug.from-dp.dump.enabled", false);
    }

    @Nullable
    public static File getDebugFromDPDumpPath ()
    {
      final String sPath = getConfig ().getAsString ("toop.debug.from-dp.dump.path");
      return sPath == null ? null : new File (sPath);
    }

    @Nullable
    public static File getDebugFromDPDumpPathIfEnabled ()
    {
      return isDebugFromDPDumpEnabled () ? getDebugFromDPDumpPath () : null;
    }

    // MessageProcessorDPOutgoingPerformer, step 3/4

    public static boolean isDebugToDCDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.debug.to-dc.dump.enabled", false);
    }

    @Nullable
    public static File getDebugToDCDumpPath ()
    {
      final String sPath = getConfig ().getAsString ("toop.debug.to-dc.dump.path");
      return sPath == null ? null : new File (sPath);
    }

    @Nullable
    public static File getDebugToDCDumpPathIfEnabled ()
    {
      return isDebugToDCDumpEnabled () ? getDebugToDCDumpPath () : null;
    }

    // MessageProcessorDCOutgoingPerformer, step 1/4

    public static boolean isDebugToDPDumpEnabled ()
    {
      return getConfig ().getAsBoolean ("toop.debug.to-dp.dump.enabled", false);
    }

    @Nullable
    public static File getDebugToDPDumpPath ()
    {
      final String sPath = getConfig ().getAsString ("toop.debug.to-dp.dump.path");
      return sPath == null ? null : new File (sPath);
    }

    @Nullable
    public static File getDebugToDPDumpPathIfEnabled ()
    {
      return isDebugToDPDumpEnabled () ? getDebugToDPDumpPath () : null;
    }
  }

  public static class HTTP
  {
    private HTTP ()
    {}

    public static boolean isUseHttpSystemProperties ()
    {
      return getConfig ().getAsBoolean ("http.usesysprops", false);
    }

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
