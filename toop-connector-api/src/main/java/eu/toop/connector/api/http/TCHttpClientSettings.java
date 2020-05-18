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
package eu.toop.connector.api.http;

import java.security.GeneralSecurityException;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.exception.InitializationException;
import com.helger.httpclient.HttpClientSettings;

import eu.toop.connector.api.TCConfig;

/**
 * Common TOOP Connector HTTPClient factory
 *
 * @author Philip Helger
 */
public class TCHttpClientSettings extends HttpClientSettings
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TCHttpClientSettings.class);

  public TCHttpClientSettings ()
  {
    if (TCConfig.HTTP.isUseHttpSystemProperties ())
    {
      // For proxy etc
      setUseSystemProperties (true);
      LOGGER.info ("Using predefined System properties to configure HTTP connection. All manually configured values for HTTP connections are not used.");
    }
    else
    {
      // Add settings from configuration file here centrally
      if (TCConfig.HTTP.isProxyServerEnabled ())
      {
        setProxyHost (new HttpHost (TCConfig.HTTP.getProxyServerAddress (), TCConfig.HTTP.getProxyServerPort ()));

        // Non-proxy hosts
        addNonProxyHostsFromPipeString (TCConfig.HTTP.getProxyServerNonProxyHosts ());
      }

      // Disable SSL checks?
      if (TCConfig.HTTP.isTLSTrustAll ())
        try
        {
          setSSLContextTrustAll ();
          setHostnameVerifierVerifyAll ();
        }
        catch (final GeneralSecurityException ex)
        {
          throw new InitializationException (ex);
        }
    }
  }
}
