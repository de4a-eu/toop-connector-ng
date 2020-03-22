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
package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Source: LevelOfAssuranceCode-CodeList.gc
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum EToopLevelOfAssurance implements IHasID <String>
{
  NONE_120 ("none"),
  // Casing changed in data model 1.4.0
  NONE ("None"),
  LOW ("Low"),
  SUBSTANTIAL ("Substantial"),
  HIGH ("High");

  private final String m_sID;

  private EToopLevelOfAssurance (@Nonnull @Nonempty final String sID)
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
  public static EToopLevelOfAssurance getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopLevelOfAssurance.class, sID);
  }
}
