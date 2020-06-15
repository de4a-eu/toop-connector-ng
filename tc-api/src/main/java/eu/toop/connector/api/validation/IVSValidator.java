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
package eu.toop.connector.api.validation;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.helger.bdve.api.executorset.VESID;
import com.helger.bdve.api.result.ValidationResultList;

/**
 * TC Validation Service
 *
 * @author Philip Helger
 */
public interface IVSValidator
{
  /**
   * Perform validation
   *
   * @param aVESID
   *        VESID to use.
   * @param aPayload
   *        Payload to validate.
   * @param aDisplayLocale
   *        Display locale for the error message.
   * @return A non-<code>null</code> result list.
   */
  @Nonnull
  ValidationResultList validate (@Nonnull VESID aVESID, @Nonnull byte [] aPayload, @Nonnull Locale aDisplayLocale);
}
