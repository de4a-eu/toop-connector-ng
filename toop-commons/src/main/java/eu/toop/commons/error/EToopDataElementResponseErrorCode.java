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
package eu.toop.commons.error;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;

/**
 * Source: DataElementResponseErrorCode-CodeList.gc<br>
 * Content created by MainCreateJavaCode_DataElementResponseErrorCode_GC
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum EToopDataElementResponseErrorCode implements IToopErrorCode
{
  /** Unknown concept */
  DP_ELE_001 ("DP_ELE_001", "Unknown concept"),
  /** Unauthorized */
  DP_ELE_002 ("DP_ELE_002", "Unauthorized"),
  /** Ambiguous concept */
  DP_ELE_003 ("DP_ELE_003", "Ambiguous concept"),
  /** Unavailable */
  DP_ELE_004 ("DP_ELE_004", "Unavailable"),
  /** Internal processing error */
  DP_ELE_005 ("DP_ELE_005", "Internal processing error"),
  /** Insufficient input */
  DP_ELE_006 ("DP_ELE_006", "Insufficient input");

  private final String m_sID;
  private final String m_sDisplayName;

  private EToopDataElementResponseErrorCode (@Nonnull @Nonempty final String sID,
                                             @Nonnull @Nonempty final String sDisplayName)
  {
    m_sID = sID;
    m_sDisplayName = sDisplayName;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nullable
  public static EToopDataElementResponseErrorCode getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopDataElementResponseErrorCode.class, sID);
  }
}
