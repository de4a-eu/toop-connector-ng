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
package eu.toop.edm.creator;

import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;

import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotConsentToken;
import eu.toop.edm.slot.SlotDataConsumer;
import eu.toop.edm.slot.SlotDatasetIdentifier;
import eu.toop.edm.slot.SlotFullfillingRequirement;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotProcedure;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.QueryType;

public class EDMRequestCreator
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotProcedure.NAME,
                                                                                                  SlotFullfillingRequirement.NAME,
                                                                                                  SlotConsentToken.NAME,
                                                                                                  SlotDatasetIdentifier.NAME,
                                                                                                  SlotDataConsumer.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final String m_sID;
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  /**
   * Constructor
   *
   * @param eQueryDefinition
   *        Query definition type to be used. May not be <code>null</code>.
   * @param aProviders
   *        All slot providers to be added. May not be <code>null</code>.
   */
  public EDMRequestCreator (@Nonnull final EQueryDefinitionType eQueryDefinition,
                             @Nonnull final String sID,
                             @Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notEmpty (sID, "ID");
    ValueEnforcer.noNullValue (aProviders, "Providers");

    m_sID = sID;
    m_eQueryDefinition = eQueryDefinition;
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }
  }

  @Nonnull
  public QueryRequest createQueryRequest ()
  {
    final QueryRequest ret = RegRepHelper.createEmptyQueryRequest ();
    ret.setId(m_sID);

    // All top-level slots outside of query
    for (final String sTopLevel : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sTopLevel);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      final QueryType aQuery = new QueryType ();
      aQuery.setQueryDefinition (m_eQueryDefinition.getAttrValue ());

      // All slots inside of query
      for (final Map.Entry <String, ISlotProvider> aEntry : m_aProviders.entrySet ())
        if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
          aQuery.addSlot (aEntry.getValue ().createSlot ());

      ret.setQuery (aQuery);
    }

    return ret;
  }
}
