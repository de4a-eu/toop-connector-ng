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
 * Source: DocumentResponseErrorCode-CodeList.gc<br>
 * Content created by MainCreateJavaCode_DocumentResponseErrorCode_GC
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum EToopDocumentResponseErrorCode implements IToopErrorCode
{
  /** Unknown document type */
  DP_DOC_001 ("DP_DOC_001", "Unknown document type"),
  /** Unauthorized */
  DP_DOC_002 ("DP_DOC_002", "Unauthorized"),
  /** Unavailable */
  DP_DOC_003 ("DP_DOC_003", "Unavailable"),
  /** Internal processing error */
  DP_DOC_004 ("DP_DOC_004", "Internal processing error"),
  /** Payload too large */
  DP_DOC_005 ("DP_DOC_005", "Payload too large"),
  /** Timeout */
  DP_DOC_006 ("DP_DOC_006", "Timeout"),
  /** Different mime type */
  DP_DOC_007 ("DP_DOC_007", "Different mime type");

  private final String m_sID;
  private final String m_sDisplayName;

  EToopDocumentResponseErrorCode (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sDisplayName)
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
  public static EToopDocumentResponseErrorCode getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopDocumentResponseErrorCode.class, sID);
  }
}
