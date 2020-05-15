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
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.xml.cccev.ConceptMarshaller;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.slot.ERegRepCollectionType;
import eu.toop.regrep.slot.ISlotProvider;
import eu.toop.regrep.slot.SlotHelper;
import eu.toop.regrep.slot.SlotBuilder;

/**
 * "ConceptValues" slot
 *
 * @author Philip Helger
 */
public class SlotConceptValues implements ISlotProvider
{
  public static final String NAME = "ConceptValues";

  private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();

  public SlotConceptValues (@Nonnull final ConceptPojo... aConcepts)
  {
    ValueEnforcer.noNullValue (aConcepts, "Concepts");
    m_aConcepts.addAll (aConcepts);
  }

  public SlotConceptValues (@Nonnull final Iterable <ConceptPojo> aConcepts)
  {
    ValueEnforcer.noNullValue (aConcepts, "Concepts");
    m_aConcepts.addAll (aConcepts);
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
    final ConceptMarshaller m = new ConceptMarshaller ();
    return new SlotBuilder ().setName (NAME)
                             .setValue (ERegRepCollectionType.SET,
                                        m_aConcepts.getAllMapped (x -> SlotHelper.createSlotValue (m.getAsDocument (x.getAsCCCEVConcept ())
                                                                                                      .getDocumentElement ())))
                             .build ();
  }
}
