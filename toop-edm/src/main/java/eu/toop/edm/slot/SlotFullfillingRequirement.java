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
package eu.toop.edm.slot;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.xml.cccev.RequirementMarshaller;
import eu.toop.regrep.SlotBuilder;
import eu.toop.regrep.rim.SlotType;

/**
 * "FullfillingRequirement" slot
 *
 * @author Philip Helger
 */
public class SlotFullfillingRequirement implements ISlotProvider
{
  public static final String NAME = "FullfillingRequirement";

  private final CCCEVRequirementType m_aRequirement;

  public SlotFullfillingRequirement (@Nonnull final CCCEVRequirementType aRequirement)
  {
    ValueEnforcer.notNull (aRequirement, "Requirement");
    m_aRequirement = aRequirement;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return NAME;
  }

  @Nonnull
  public SlotType createSlot ()
  {
    return new SlotBuilder ().setName (NAME)
                             .setValue (new RequirementMarshaller ().getAsDocument (m_aRequirement)
                                                                    .getDocumentElement ())
                             .build ();
  }
}
