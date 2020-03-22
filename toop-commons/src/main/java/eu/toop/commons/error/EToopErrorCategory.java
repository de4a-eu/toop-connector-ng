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
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Source: ErrorCategory-CodeList.gc<br>
 * Content created by MainCreateJavaCode_ErrorCategory_GC
 *
 * @author Philip Helger
 * @since 0.9.2
 */
public enum EToopErrorCategory implements IHasID <String>
{
  /** Semantic Mapping */
  SEMANTIC_MAPPING ("SemanticMapping"),
  /** Parsing */
  PARSING ("Parsing"),
  /** Dynamic Discovery */
  DYNAMIC_DISCOVERY ("DynamicDiscovery"),
  /** eDelivery */
  E_DELIVERY ("eDelivery"),
  /** Results Aggregation */
  RESULTS_AGGREGATION ("ResultsAggregation"),
  /** Business Processing */
  BUSINESS_PROCESSING ("BusinessProcessing"),
  /** Technical Error */
  TECHNICAL_ERROR ("TechnicalError");

  /** Typo in enum name up to 0.10.0 */
  @Deprecated
  public static final EToopErrorCategory RESULT_AGGREGATION = RESULTS_AGGREGATION;

  private final String m_sID;

  private EToopErrorCategory (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EToopErrorCategory getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopErrorCategory.class, sID);
  }
}
