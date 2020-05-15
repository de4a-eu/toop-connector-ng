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

import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.xml.dcatap.DistributionMarshaller;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.slot.ERegRepCollectionType;
import eu.toop.regrep.slot.ISlotProvider;
import eu.toop.regrep.slot.SlotHelper;
import eu.toop.regrep.slot.SlotBuilder;

/**
 * "DistributionRequestList" slot
 *
 * @author Philip Helger
 */
public class SlotDistributionRequestList implements ISlotProvider
{
  public static final String NAME = "DistributionRequestList";

  private final ICommonsList <DistributionPojo> m_aDistributions = new CommonsArrayList <> ();

  public SlotDistributionRequestList (@Nonnull final DistributionPojo... aDistributions)
  {
    ValueEnforcer.noNullValue (aDistributions, "Distributions");
    m_aDistributions.addAll (aDistributions);
  }

  public SlotDistributionRequestList (@Nonnull final Iterable <DistributionPojo> aDistributions)
  {
    ValueEnforcer.noNullValue (aDistributions, "Distributions");
    m_aDistributions.addAll (aDistributions);
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
    final DistributionMarshaller m = new DistributionMarshaller ();
    return new SlotBuilder ().setName (NAME)
                             .setValue (ERegRepCollectionType.SORTED_SET,
                                        m_aDistributions.getAllMapped (x -> SlotHelper.createSlotValue (m.getAsDocument (x.getAsDistribution ())
                                                                                                           .getDocumentElement ())))
                             .build ();
  }
}
