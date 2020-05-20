/**
 * Copyright (C) 2019-2020 toop.eu
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
package eu.toop.connector.mem.phase4;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.debug.GlobalDebug;
import com.helger.config.IConfig;
import com.helger.phase4.crypto.AS4CryptoFactoryPropertiesFile;
import com.helger.phase4.crypto.AS4CryptoProperties;
import com.helger.phase4.crypto.IAS4CryptoFactory;
import com.helger.security.keystore.EKeyStoreType;

import eu.toop.connector.api.TCConfig;

/**
 * Wrapper to access the configuration for the phase4 module.
 *
 * @author Philip Helger
 */
public final class Phase4Config
{
  private Phase4Config ()
  {}

  @Nonnull
  public static IConfig getConfig ()
  {
    return TCConfig.getConfig ();
  }

  @Nullable
  public static String getDataPath ()
  {
    return getConfig ().getAsString ("phase4.datapath");
  }

  @Nullable
  public static String getFromPartyID ()
  {
    return TCConfig.MEM.getMEMAS4TcPartyid ();
  }

  public static boolean isHttpDebugEnabled ()
  {
    return getConfig ().getAsBoolean ("phase4.debug.http", false);
  }

  public static boolean isDebugIncoming ()
  {
    return getConfig ().getAsBoolean ("phase4.debug.incoming", GlobalDebug.isDebugMode ());
  }

  /**
   * @return Optional folder where to store responses to
   */
  @Nullable
  public static String getSendResponseFolderName ()
  {
    // Can be relative or absolute
    return getConfig ().getAsString ("phase4.send.response.folder");
  }

  // Keystore stuff
  @Nonnull
  public static EKeyStoreType getKeyStoreType ()
  {
    return EKeyStoreType.getFromIDCaseInsensitiveOrDefault (getConfig ().getAsString ("phase4.keystore.type"), EKeyStoreType.JKS);
  }

  @Nullable
  public static String getKeyStorePath ()
  {
    return getConfig ().getAsString ("phase4.keystore.path");
  }

  @Nullable
  public static String getKeyStorePassword ()
  {
    return getConfig ().getAsString ("phase4.keystore.password");
  }

  @Nullable
  public static String getKeyStoreKeyAlias ()
  {
    return getConfig ().getAsString ("phase4.keystore.key-alias");
  }

  @Nullable
  public static String getKeyStoreKeyPassword ()
  {
    return getConfig ().getAsString ("phase4.keystore.key-password");
  }

  // Truststore stuff
  @Nonnull
  public static EKeyStoreType getTrustStoreType ()
  {
    return EKeyStoreType.getFromIDCaseInsensitiveOrDefault (getConfig ().getAsString ("phase4.truststore.type"), EKeyStoreType.JKS);
  }

  @Nullable
  public static String getTrustStorePath ()
  {
    return getConfig ().getAsString ("phase4.truststore.path");
  }

  @Nullable
  public static String getTrustStorePassword ()
  {
    return getConfig ().getAsString ("phase4.truststore.password");
  }

  @Nonnull
  public static IAS4CryptoFactory getCryptoFactory ()
  {
    return new AS4CryptoFactoryPropertiesFile (new AS4CryptoProperties ().setKeyStoreType (getKeyStoreType ())
                                                                         .setKeyStorePath (getKeyStorePath ())
                                                                         .setKeyStorePassword (getKeyStorePassword ())
                                                                         .setKeyAlias (getKeyStoreKeyAlias ())
                                                                         .setKeyPassword (getKeyStoreKeyPassword ())
                                                                         .setTrustStoreType (getTrustStoreType ())
                                                                         .setTrustStorePath (getTrustStorePath ())
                                                                         .setTrustStorePassword (getTrustStorePassword ()));
  }
}
