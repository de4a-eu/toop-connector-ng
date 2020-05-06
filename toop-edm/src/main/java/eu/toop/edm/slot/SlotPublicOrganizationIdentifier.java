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

import eu.toop.regrep.SlotBuilder;
import eu.toop.regrep.helper.VocabularyTerm;
import eu.toop.regrep.rim.SlotType;

/**
 * Error "PublicOrganizationIdentifier" slot
 *
 * @author Philip Helger
 */
public class SlotPublicOrganizationIdentifier implements ISlotProvider
{
  public static final String NAME = "PublicOrganizationIdentifier";

  private final VocabularyTerm m_aValue;

  public SlotPublicOrganizationIdentifier (@Nonnull final VocabularyTerm aValue)
  {
    ValueEnforcer.notNull (aValue, "Value");
    m_aValue = aValue;
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
    return new SlotBuilder ().setName (NAME).setValue (m_aValue).build ();
  }
}
