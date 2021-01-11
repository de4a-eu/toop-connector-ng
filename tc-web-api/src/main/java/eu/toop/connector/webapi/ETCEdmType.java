/**
 * Copyright (C) 2018-2021 toop.eu. All rights reserved.
 *
 * This project is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.connector.webapi;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.phive.api.executorset.VESID;

import eu.toop.connector.api.rest.TCPayloadType;
import eu.toop.connector.app.validation.TCValidationRules;

/**
 * Contains the different top-level document types.
 *
 * @author Philip Helger
 */
public enum ETCEdmType
{
  REQUEST ("req", TCValidationRules.VID_TOOP_EDM_REQUEST_210, TCPayloadType.REQUEST),
  RESPONSE ("resp", TCValidationRules.VID_TOOP_EDM_RESPONSE_210, TCPayloadType.RESPONSE),
  ERROR_RESPONSE ("errresp", TCValidationRules.VID_TOOP_EDM_ERROR_RESPONSE_210, TCPayloadType.ERROR_RESPONSE);

  private final String m_sID;
  private final VESID m_aVESID;
  private final TCPayloadType m_ePayloadType;

  ETCEdmType (@Nonnull @Nonempty final String sID, @Nonnull final VESID aVESID, @Nonnull final TCPayloadType ePayloadType)
  {
    m_sID = sID;
    m_aVESID = aVESID;
    m_ePayloadType = ePayloadType;
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

  @Nonnull
  public TCPayloadType getPayloadType ()
  {
    return m_ePayloadType;
  }
}
