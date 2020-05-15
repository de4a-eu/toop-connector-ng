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

import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.slot.ISlotProvider;
import eu.toop.regrep.slot.SlotBuilder;

/**
 * "ErrorProvider" slot
 *
 * @author Philip Helger
 */
public class SlotErrorProvider implements ISlotProvider
{
  public static final String NAME = "ErrorProvider";

  private final AgentType m_aAgent;

  public SlotErrorProvider (@Nonnull final AgentType aAgent)
  {
    ValueEnforcer.notNull (aAgent, "Agent");
    m_aAgent = aAgent;
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
                             .setValue (new AgentMarshaller ().getAsDocument (m_aAgent).getDocumentElement ())
                             .build ();
  }
}
