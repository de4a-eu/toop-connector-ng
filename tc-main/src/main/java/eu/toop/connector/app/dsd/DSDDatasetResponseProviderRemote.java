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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsHashSet;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.string.ToStringGenerator;
import eu.toop.connector.api.TCConfig;
import eu.toop.connector.api.dd.IDDErrorHandler;
import eu.toop.connector.api.dsd.IDSDDatasetResponseProvider;
import eu.toop.connector.api.dsd.IDSDParticipantIDProvider;
import eu.toop.connector.api.dsd.model.DatasetResponse;
import eu.toop.connector.api.http.TCHttpClientSettings;
import eu.toop.dsd.client.DSDClient;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class implements the {@link IDSDParticipantIDProvider} interface using a
 * remote query to DSD.
 *
 * @author Philip Helger
 */
public class DSDDatasetResponseProviderRemote implements IDSDDatasetResponseProvider
{
  private final String m_sBaseURL;

  /**
   * Constructor using the TOOP Directory URL from the configuration file.
   */
  public DSDDatasetResponseProviderRemote()
  {
    this (TCConfig.DSD.getR2D2DirectoryBaseUrl ());
  }

  /**
   * Constructor with an arbitrary TOOP Directory URL.
   *
   * @param sBaseURL
   *        The base URL to be used. May neither be <code>null</code> nor empty.
   */
  public DSDDatasetResponseProviderRemote(@Nonnull final String sBaseURL)
  {
    ValueEnforcer.notEmpty (sBaseURL, "BaseURL");
    m_sBaseURL = sBaseURL;
  }

  /**
   * @return The TOOP Directory Base URL as provided in the constructor. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public final String getBaseURL ()
  {
    return m_sBaseURL;
  }

  @Nonnull
  public ICommonsSet <DatasetResponse> getAllDatasetResponses (@Nonnull final String sLogPrefix,
                                                                    @Nonnull final String sDatasetType,
                                                                    @Nullable final String sCountryCode,
                                                                    @Nonnull final IDDErrorHandler aErrorHandler)
  {
    final ICommonsSet <DatasetResponse> ret = new CommonsHashSet <> ();

    final DSDClient aDSDClient = new DSDClient (m_sBaseURL);
    aDSDClient.setHttpClientSettings (new TCHttpClientSettings ());

    List <DCatAPDatasetType> datasetTypesList = null;
    try
    {
      datasetTypesList = aDSDClient.queryDataset (sDatasetType, sCountryCode);
      datasetTypesList.forEach(
          d -> {
            d.getDistribution().forEach(
                    dist -> {
                      DatasetResponse resp = new DatasetResponse();
                      // Access Service Conforms To
                      if (dist.getAccessService().getConformsToCount() > 0) {
                        resp.setAccessServiceConforms(dist.getAccessService().getConformsToAtIndex(0).getValue());
                      }

                      // Access Service Identifier, used as Document Type ID
                  //    resp.setDocumentTypeIdentifier(new Siantdist.getAccessService().getIdentifier()
                    });
          });
    }
    catch (final RuntimeException ex)
    {
      aErrorHandler.onError ("Failed to query the DSD", ex, EToopErrorCode.DD_001);
    }

    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("BaseURL", m_sBaseURL).getToString ();
  }
}
