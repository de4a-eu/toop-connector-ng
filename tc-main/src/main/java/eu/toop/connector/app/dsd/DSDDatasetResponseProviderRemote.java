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
package eu.toop.connector.app.dsd;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.dd.IDDErrorHandler;
import eu.toop.connector.api.dsd.DSDDatasetHelper;
import eu.toop.connector.api.dsd.DSDDatasetResponse;
import eu.toop.connector.api.dsd.IDSDDatasetResponseProvider;
import eu.toop.connector.api.http.TCHttpClientSettings;
import eu.toop.dsd.client.DSDClient;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;

/**
 * This class implements the {@link IDSDDatasetResponseProvider} interface using
 * a remote query to DSD.
 *
 * @author Philip Helger
 */
public class DSDDatasetResponseProviderRemote implements IDSDDatasetResponseProvider
{
  private static final Logger LOGGER = LoggerFactory.getLogger (DSDDatasetResponseProviderRemote.class);

  private final String m_sBaseURL;

  /**
   * Constructor using the DSD URL from the configuration file.
   */
  public DSDDatasetResponseProviderRemote ()
  {
    this (TCConfig.DSD.getDSDBaseUrl ());
  }

  /**
   * Constructor with an arbitrary DSD URL.
   *
   * @param sBaseURL
   *        The base URL to be used. May neither be <code>null</code> nor empty.
   */
  public DSDDatasetResponseProviderRemote (@Nonnull final String sBaseURL)
  {
    ValueEnforcer.notEmpty (sBaseURL, "BaseURL");
    m_sBaseURL = sBaseURL;
  }

  /**
   * @return The DSD Base URL as provided in the constructor. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public final String getBaseURL ()
  {
    return m_sBaseURL;
  }

  @Nonnull
  public ICommonsSet <DSDDatasetResponse> getAllDatasetResponses (@Nonnull final String sLogPrefix,
                                                                  @Nonnull final String sDatasetType,
                                                                  @Nullable final String sCountryCode,
                                                                  @Nonnull final IDDErrorHandler aErrorHandler)
  {
    final DSDClient aDSDClient = new DSDClient (m_sBaseURL);
    aDSDClient.setHttpClientSettings (new TCHttpClientSettings ());

    try
    {
      final List <DCatAPDatasetType> datasetTypesList = aDSDClient.queryDataset (sDatasetType, sCountryCode);
      return DSDDatasetHelper.buildDSDResponseSet (datasetTypesList);
    }
    catch (final RuntimeException ex)
    {
      LOGGER.error (ex.getMessage (), ex);
      aErrorHandler.onError ("Failed to query the DSD", ex, EToopErrorCode.DD_001);
    }

    // return EMPTY result set.
    return new CommonsHashSet <> ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("BaseURL", m_sBaseURL).getToString ();
  }
}
