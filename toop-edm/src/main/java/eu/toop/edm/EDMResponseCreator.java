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
package eu.toop.edm;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.edm.slot.SlotDataProvider;
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.RegistryObjectListType;
import eu.toop.regrep.rim.RegistryObjectType;

/**
 * A simple builder to create valid TOOP Responses for both "concept queries"
 * and for "document queries". See {@link #builderConcept()} and
 * {@link #builderDocument()}.
 *
 * @author Philip Helger
 */
public class EDMResponseCreator
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotDataProvider.NAME);

  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  private EDMResponseCreator (@Nonnull final ERegRepResponseStatus eResponseStatus,
                              @Nonnull @Nonempty final String sRequestID,
                              @Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.noNullValue (aProviders, "Providers");

    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;

    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }
  }

  @Nonnull
  QueryResponse createQueryResponse ()
  {
    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse (m_eResponseStatus);
    ret.setRequestId (m_sRequestID);

    // All top-level slots outside of object list
    for (final String sHeader : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      final RegistryObjectListType aROList = new RegistryObjectListType ();
      final RegistryObjectType aRO = new RegistryObjectType ();
      aRO.setId (UUID.randomUUID ().toString ());

      // All slots inside of RegistryObject
      for (final Map.Entry <String, ISlotProvider> aEntry : m_aProviders.entrySet ())
        if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
          aRO.addSlot (aEntry.getValue ().createSlot ());

      aROList.addRegistryObject (aRO);
      ret.setRegistryObjectList (aROList);
    }

    return ret;
  }

  @Nonnull
  public static Builder builder ()
  {
    // Use the default specification identifier
    return new Builder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  @Nonnull
  public static Builder builderConcept ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.CONCEPT);
  }

  @Nonnull
  public static Builder builderDocument ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.DOCUMENT);
  }

  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private ERegRepResponseStatus m_eResponseStatus;
    private String m_sRequestID;
    private String m_sSpecificationIdentifier;
    private LocalDateTime m_aIssueDateTime;
    private AgentType m_aDataProvider;
    private CCCEVConceptType m_aConcept;
    private DCatAPDatasetType m_aDataset;

    public Builder ()
    {}

    @Nonnull
    public Builder queryDefinition (@Nullable final EQueryDefinitionType e)
    {
      m_eQueryDefinition = e;
      return this;
    }

    @Nonnull
    public Builder responseStatus (@Nullable final ERegRepResponseStatus e)
    {
      m_eResponseStatus = e;
      return this;
    }

    @Nonnull
    public Builder requestID (@Nullable final String s)
    {
      m_sRequestID = s;
      return this;
    }

    @Nonnull
    public Builder specificationIdentifier (@Nullable final String s)
    {
      m_sSpecificationIdentifier = s;
      return this;
    }

    @Nonnull
    public Builder issueDateTimeNow ()
    {
      return issueDateTime (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder issueDateTime (@Nullable final LocalDateTime a)
    {
      m_aIssueDateTime = a;
      return this;
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentPojo.Builder a)
    {
      return dataProvider (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentPojo a)
    {
      return dataProvider (a == null ? null : a.getAsAgent ());
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentType a)
    {
      m_aDataProvider = a;
      return this;
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo a)
    {
      return concept (a == null ? null : a.getAsCCCEVConcept ());
    }

    @Nonnull
    public Builder concept (@Nullable final CCCEVConceptType a)
    {
      m_aConcept = a;
      return this;
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo.Builder a)
    {
      return dataset (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo a)
    {
      return dataset (a == null ? null : a.getAsDataset ());
    }

    @Nonnull
    public Builder dataset (@Nullable final DCatAPDatasetType a)
    {
      m_aDataset = a;
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eQueryDefinition == null)
        throw new IllegalStateException ("Query Definition must be present");
      if (m_eResponseStatus == null)
        throw new IllegalStateException ("Response Status must be present");
      if (StringHelper.hasNoText (m_sRequestID))
        throw new IllegalStateException ("Request ID must be present");
      if (StringHelper.hasNoText (m_sSpecificationIdentifier))
        throw new IllegalStateException ("SpecificationIdentifier must be present");
      if (m_aIssueDateTime == null)
        throw new IllegalStateException ("Issue Date Time must be present");
      if (m_aDataProvider == null)
        throw new IllegalStateException ("Data Provider must be present");

      switch (m_eQueryDefinition)
      {
        case CONCEPT:
          if (m_aConcept == null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must contain a Concept");
          if (m_aDataset != null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain a Dataset");
          break;
        case DOCUMENT:
          if (m_aConcept != null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain a Concept");
          if (m_aDataset == null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must contain a Dataset");
          break;
        default:
          throw new IllegalStateException ("Unhandled query definition " + m_eQueryDefinition);
      }
    }

    @Nonnull
    public QueryResponse build ()
    {
      checkConsistency ();

      final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();
      if (m_sSpecificationIdentifier != null)
        aSlots.add (new SlotSpecificationIdentifier (m_sSpecificationIdentifier));
      if (m_aIssueDateTime != null)
        aSlots.add (new SlotIssueDateTime (m_aIssueDateTime));
      if (m_aDataProvider != null)
        aSlots.add (new SlotDataProvider (m_aDataProvider));

      // ConceptValues
      if (m_aConcept != null)
        aSlots.add (new SlotConceptValues (m_aConcept));

      // DocumentMetadata
      if (m_aDataset != null)
        aSlots.add (new SlotDocumentMetadata (m_aDataset));

      return new EDMResponseCreator (m_eResponseStatus, m_sRequestID, aSlots).createQueryResponse ();
    }
  }
}
