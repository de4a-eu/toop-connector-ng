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
package eu.toop.connector.api.dsd;

import com.helger.commons.collection.impl.ICommonsSet;
import eu.toop.connector.api.dd.IDDErrorHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface to resolve Country Code and Dataset Type ID to a set of matching
 * DSD Dataset Responses.<br>
 * Am implementation of this class e.g. queries the DSD for the
 * respective participants.
 *
 * @author jerouris
 */

public interface IDSDDatasetResponseProvider
{
  /**
   * Get all DSD Responses that match the provided country code and document
   * type ID.
   *
   * @param sLogPrefix
   *        The logging prefix to be used. May not be <code>null</code>.
   * @param sDatasetType
   *        Dataset Type to query. May not be <code>null</code>.
   * @param sCountryCode
   *        Country code to use. Must be a 2-digit string. May be
   *        <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of Participant IDs.
   */
  @Nonnull
  ICommonsSet <DSDDatasetResponse> getAllDatasetResponses (@Nonnull String sLogPrefix,
                                                             @Nonnull String sDatasetType,
                                                             @Nullable String sCountryCode,
                                                             @Nonnull IDDErrorHandler aErrorHandler);
}
