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
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.edm.slot.SlotDataProvider;
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.RegistryObjectListType;
import eu.toop.regrep.rim.RegistryObjectType;

/**
 * This class contains the data model for a single TOOP EDM Request. It requires
 * at least the following fields:
 * <ul>
 * <li>QueryDefinition - Concept or Document query?</li>
 * <li>Response Status - Success, partial success or failure</li>
 * <li>Request ID - the ID of the request to which this response
 * correlates.</li>
 * <li>Specification Identifier - must be the value
 * {@link CToopEDM#SPECIFICATION_IDENTIFIER_TOOP_EDM_V20}.</li>
 * <li>Issue date time - when the response was created. Ideally in UTC.</li>
 * <li>Data Provider - the basic infos of the DP</li>
 * <li>If it is a "ConceptQuery" the response Concepts must be provided.</li>
 * <li>If it is a "DocumentQuery" the response Dataset must be provided.</li>
 * </ul>
 * It is recommended to use the {@link #builder()} methods to create the EDM
 * request using the builder pattern with a fluent API.
 *
 * @author Philip Helger
 * @author Konstantinos Douloudis
 */
public class EDMResponse
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotDataProvider.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final String m_sSpecificationIdentifier;
  private final LocalDateTime m_aIssueDateTime;
  private final AgentPojo m_aDataProvider;
  private final ConceptPojo m_aConcept;
  private final DatasetPojo m_aDataset;

  public EDMResponse (@Nonnull final EQueryDefinitionType eQueryDefinition,
                      @Nonnull final ERegRepResponseStatus eResponseStatus,
                      @Nonnull @Nonempty final String sRequestID,
                      @Nonnull @Nonempty final String sSpecificationIdentifier,
                      @Nonnull final LocalDateTime aIssueDateTime,
                      @Nonnull final AgentPojo aDataProvider,
                      @Nullable final ConceptPojo aConcept,
                      @Nullable final DatasetPojo aDataset)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.notNull (aDataProvider, "DataProvider");
    ValueEnforcer.isFalse ((aConcept == null && aDataset == null) || (aConcept != null && aDataset != null),
                           "Exactly one of Concept and Dataset must be set");
    switch (eQueryDefinition)
    {
      case CONCEPT:
        ValueEnforcer.notNull (aConcept, "Concept");
        break;
      case DOCUMENT:
        ValueEnforcer.notNull (aDataset, "Dataset");
        break;
      default:
        throw new IllegalArgumentException ("Unsupported query definition: " + eQueryDefinition);
    }

    m_eQueryDefinition = eQueryDefinition;
    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aIssueDateTime = aIssueDateTime;
    m_aDataProvider = aDataProvider;
    m_aConcept = aConcept;
    m_aDataset = aDataset;
  }

  @Nonnull
  public EQueryDefinitionType getQueryDefinition ()
  {
    return m_eQueryDefinition;
  }

  @Nonnull
  public ERegRepResponseStatus getResponseStatus ()
  {
    return m_eResponseStatus;
  }

  @Nonnull
  @Nonempty
  public String getRequestID ()
  {
    return m_sRequestID;
  }

  @Nonnull
  @Nonempty
  public String getSpecificationIdentifier ()
  {
    return m_sSpecificationIdentifier;
  }

  @Nonnull
  public LocalDateTime getIssueDateTime ()
  {
    return m_aIssueDateTime;
  }

  @Nonnull
  public AgentPojo getDataProvider ()
  {
    return m_aDataProvider;
  }

  @Nullable
  public ConceptPojo getConcept ()
  {
    return m_aConcept;
  }

  @Nullable
  public DatasetPojo getDataset ()
  {
    return m_aDataset;
  }

  @Nonnull
  private QueryResponse _createQueryResponse (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    final ICommonsOrderedMap <String, ISlotProvider> aProviderMap = new CommonsLinkedHashMap <> ();
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (aProviderMap.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      aProviderMap.put (sName, aItem);
    }

    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse (m_eResponseStatus);
    ret.setRequestId (m_sRequestID);

    // All top-level slots outside of object list
    for (final String sHeader : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = aProviderMap.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      final RegistryObjectListType aROList = new RegistryObjectListType ();
      final RegistryObjectType aRO = new RegistryObjectType ();
      aRO.setId (UUID.randomUUID ().toString ());

      // All slots inside of RegistryObject
      for (final Map.Entry <String, ISlotProvider> aEntry : aProviderMap.entrySet ())
        if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
          aRO.addSlot (aEntry.getValue ().createSlot ());

      aROList.addRegistryObject (aRO);
      ret.setRegistryObjectList (aROList);
    }

    return ret;
  }

  @Nonnull
  public QueryResponse getAsQueryResponse ()
  {
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

    return _createQueryResponse (aSlots);
  }

  @Nonnull
  public IVersatileWriter <QueryResponse> getWriter ()
  {
    return new JAXBVersatileWriter <> (getAsQueryResponse (),
                                       RegRep4Writer.queryResponse (CCCEV.XSDS).setFormattedOutput (true));
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || getClass () != o.getClass ())
      return false;
    final EDMResponse that = (EDMResponse) o;
    return EqualsHelper.equals (m_eQueryDefinition, that.m_eQueryDefinition) &&
           EqualsHelper.equals (m_eResponseStatus, that.m_eResponseStatus) &&
           EqualsHelper.equals (m_sRequestID, that.m_sRequestID) &&
           EqualsHelper.equals (m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
           EqualsHelper.equals (m_aIssueDateTime, that.m_aIssueDateTime) &&
           EqualsHelper.equals (m_aDataProvider, that.m_aDataProvider) &&
           EqualsHelper.equals (m_aConcept, that.m_aConcept) &&
           EqualsHelper.equals (m_aDataset, that.m_aDataset);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eQueryDefinition)
                                       .append (m_eResponseStatus)
                                       .append (m_sRequestID)
                                       .append (m_sSpecificationIdentifier)
                                       .append (m_aIssueDateTime)
                                       .append (m_aDataProvider)
                                       .append (m_aConcept)
                                       .append (m_aDataset)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("QueryDefinition", m_eQueryDefinition)
                                       .append ("RequestID", m_sRequestID)
                                       .append ("ResponseStatus", m_eResponseStatus)
                                       .append ("SpecificationIdentifier", m_sSpecificationIdentifier)
                                       .append ("IssueDateTime", m_aIssueDateTime)
                                       .append ("DataProvider", m_aDataProvider)
                                       .append ("Concept", m_aConcept)
                                       .append ("Dataset", m_aDataset)
                                       .getToString ();
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
    private AgentPojo m_aDataProvider;
    private ConceptPojo m_aConcept;
    private DatasetPojo m_aDataset;

    protected Builder ()
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
    public Builder requestID (@Nullable final UUID a)
    {
      return requestID (a == null ? null : a.toString ());
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
      m_aDataProvider = a;
      return this;
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentType a)
    {
      return dataProvider (a == null ? null : AgentPojo.builder (a));
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo a)
    {
      m_aConcept = a;
      return this;
    }

    @Nonnull
    public Builder concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo.Builder a)
    {
      return dataset (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo a)
    {
      m_aDataset = a;
      return this;
    }

    @Nonnull
    public Builder dataset (@Nullable final DCatAPDatasetType a)
    {
      return dataset (a == null ? null : DatasetPojo.builder (a));
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
    public EDMResponse build ()
    {
      checkConsistency ();

      return new EDMResponse (m_eQueryDefinition,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              m_aConcept,
                              m_aDataset);
    }
  }
}
