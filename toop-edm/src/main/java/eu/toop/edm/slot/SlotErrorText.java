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
import com.helger.commons.text.IMultilingualText;

import eu.toop.regrep.ERegRepCollectionType;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.SlotBuilder;
import eu.toop.regrep.rim.SlotType;

/**
 * "ErrorText" slot
 *
 * @author Philip Helger
 */
public class SlotErrorText implements ISlotProvider
{
  public static final String NAME = "ErrorText";

  private final ICommonsList <IMultilingualText> m_aValues = new CommonsArrayList <> ();

  public SlotErrorText (@Nonnull final IMultilingualText... aValues)
  {
    ValueEnforcer.noNullValue (aValues, "Values");
    m_aValues.addAll (aValues);
  }

  public SlotErrorText (@Nonnull final Iterable <IMultilingualText> aValues)
  {
    ValueEnforcer.noNullValue (aValues, "Values");
    m_aValues.addAll (aValues);
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
                             .setValue (ERegRepCollectionType.SORTED_SET,
                                        m_aValues.getAllMapped (x -> RegRepHelper.createSlotValue (RegRepHelper.createInternationalStringType (x))))
                             .build ();
  }
}
