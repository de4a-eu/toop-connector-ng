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
package eu.toop.connector.webapi;

import javax.annotation.Nonnull;

import com.helger.bdve.executorset.VESID;
import com.helger.commons.annotation.Nonempty;

import eu.toop.connector.app.validation.TCValidationRules;

/**
 * Contains the different top-level document types.
 *
 * @author Philip Helger
 */
public enum ETCEdmType
{
  REQUEST ("req", TCValidationRules.VID_TOOP_EDM_REQUEST_200),
  RESPONSE ("resp", TCValidationRules.VID_TOOP_EDM_RESPONSE_200),
  ERROR_RESPONSE ("errresp", TCValidationRules.VID_TOOP_EDM_ERROR_RESPONSE_200);

  private final String m_sID;
  private final VESID m_aVESID;

  ETCEdmType (@Nonnull @Nonempty final String sID, @Nonnull final VESID aVESID)
  {
    m_sID = sID;
    m_aVESID = aVESID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return The validation key to be used for validating this top-level type.
   */
  @Nonnull
  public VESID getVESID ()
  {
    return m_aVESID;
  }
}
