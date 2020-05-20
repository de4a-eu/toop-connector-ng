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
package eu.toop.connector.app.validation;

import javax.annotation.Nonnull;

import com.helger.bdve.executorset.VESID;
import com.helger.commons.annotation.Nonempty;

public enum EValidationEdmType
{
  REQUEST ("req", ToopEdm2Validation.VID_TOOP_EDM_REQUEST_200),
  RESPONSE ("resp", ToopEdm2Validation.VID_TOOP_EDM_RESPONSE_200),
  ERROR_RESPONSE ("errresp", ToopEdm2Validation.VID_TOOP_EDM_ERROR_RESPONSE_200);

  private final String m_sID;
  private final VESID m_aVESID;

  EValidationEdmType (@Nonnull @Nonempty final String sID, @Nonnull final VESID aVESID)
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

  @Nonnull
  public VESID getVESID ()
  {
    return m_aVESID;
  }
}
