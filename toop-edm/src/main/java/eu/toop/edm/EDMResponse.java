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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.toop.edm.model.*;
import eu.toop.regrep.rim.*;
import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.CollectionHelper;
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
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.edm.slot.SlotDataProvider;
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileReader;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.edm.xml.cccev.ConceptMarshaller;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;

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
  private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
  private final DatasetPojo m_aDataset;
  private final RepositoryItemRefPojo m_aRepositoryItemRef;

  public EDMResponse (@Nonnull final EQueryDefinitionType eQueryDefinition,
                      @Nonnull final ERegRepResponseStatus eResponseStatus,
                      @Nonnull @Nonempty final String sRequestID,
                      @Nonnull @Nonempty final String sSpecificationIdentifier,
                      @Nonnull final LocalDateTime aIssueDateTime,
                      @Nonnull final AgentPojo aDataProvider,
                      @Nullable final ICommonsList <ConceptPojo> aConcepts,
                      @Nullable final DatasetPojo aDataset,
                      @Nullable final RepositoryItemRefPojo aRepositoryItemRef)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.isTrue (eResponseStatus == ERegRepResponseStatus.SUCCESS ||
                          eResponseStatus == ERegRepResponseStatus.FAILURE,
                          "Only SUCCESS and FAILURE are supported");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.notNull (aDataProvider, "DataProvider");
    final int nConcepts = CollectionHelper.getSize (aConcepts);
    ValueEnforcer.isFalse ((nConcepts == 0 && aDataset == null) || (nConcepts != 0 && aDataset != null),
                           "Exactly one of Concept and Dataset must be set");
    switch (eQueryDefinition)
    {
      case CONCEPT:
        ValueEnforcer.notEmpty (aConcepts, "Concept");
        break;
      case DOCUMENT:
      case OBJECTREF:
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
    if (aConcepts != null)
      m_aConcepts.addAll (aConcepts);
    m_aDataset = aDataset;
    m_aRepositoryItemRef = aRepositoryItemRef;
  }

  @Nonnull
  public final EQueryDefinitionType getQueryDefinition ()
  {
    return m_eQueryDefinition;
  }

  @Nonnull
  public final ERegRepResponseStatus getResponseStatus ()
  {
    return m_eResponseStatus;
  }

  @Nonnull
  @Nonempty
  public final String getRequestID ()
  {
    return m_sRequestID;
  }

  @Nonnull
  @Nonempty
  public final String getSpecificationIdentifier ()
  {
    return m_sSpecificationIdentifier;
  }

  @Nonnull
  public final LocalDateTime getIssueDateTime ()
  {
    return m_aIssueDateTime;
  }

  @Nonnull
  public final AgentPojo getDataProvider ()
  {
    return m_aDataProvider;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <ConceptPojo> concepts ()
  {
    return m_aConcepts;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ConceptPojo> getAllConcepts ()
  {
    return m_aConcepts.getClone ();
  }

  @Nullable
  public final DatasetPojo getDataset ()
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
      if(m_eQueryDefinition.equals(EQueryDefinitionType.OBJECTREF))
      {
        final ObjectRefListType aORList = new ObjectRefListType ();
        final ObjectRefType aOR = new ObjectRefType ();

        aOR.setId (UUID.randomUUID ().toString ());

        // All slots inside of RegistryObject
        for (final Map.Entry <String, ISlotProvider> aEntry : aProviderMap.entrySet ())
          if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
            aOR.addSlot (aEntry.getValue ().createSlot ());

        aORList.addObjectRef (aOR);
        ret.setObjectRefList (aORList);
      }
      else
      {
        final RegistryObjectListType aROList = new RegistryObjectListType ();
        final ExtrinsicObjectType aEO = new ExtrinsicObjectType ();
        final SimpleLinkType aSL = new SimpleLinkType ();

        if(m_aRepositoryItemRef!=null)
          aEO.setRepositoryItemRef(m_aRepositoryItemRef.getAsSimpleLink ());

        aEO.setId (UUID.randomUUID ().toString ());

        // All slots inside of RegistryObject
        for (final Map.Entry <String, ISlotProvider> aEntry : aProviderMap.entrySet ())
          if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
            aEO.addSlot (aEntry.getValue ().createSlot ());

        aROList.addRegistryObject (aEO);
        ret.setRegistryObjectList (aROList);
      }

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
    if (m_aConcepts.isNotEmpty ())
      aSlots.add (new SlotConceptValues (m_aConcepts));

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

  @Nonnull
  public static IJAXBVersatileReader <EDMResponse> getReader ()
  {
    return new JAXBVersatileReader <> (RegRep4Reader.queryResponse (CCCEV.XSDS), EDMResponse::create);
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
           EqualsHelper.equals (m_aConcepts, that.m_aConcepts) &&
           EqualsHelper.equals (m_aDataset, that.m_aDataset) &&
           EqualsHelper.equals (m_aRepositoryItemRef, that.m_aRepositoryItemRef);
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
                                       .append (m_aConcepts)
                                       .append (m_aDataset)
                                       .append (m_aRepositoryItemRef)
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
                                       .append ("Concepts", m_aConcepts)
                                       .append ("Dataset", m_aDataset)
                                       .append ("RepositoryItemRef", m_aRepositoryItemRef)
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

  @Nonnull
  public static Builder builderDocumentRef ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.OBJECTREF);
  }

  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private ERegRepResponseStatus m_eResponseStatus;
    private String m_sRequestID;
    private String m_sSpecificationIdentifier;
    private LocalDateTime m_aIssueDateTime;
    private AgentPojo m_aDataProvider;
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
    private DatasetPojo m_aDataset;
    private RepositoryItemRefPojo m_aRepositoryItemRef;

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
      m_aIssueDateTime = a == null ? null : a.truncatedTo (ChronoUnit.MILLIS);
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
    public Builder addConcept (@Nullable final CCCEVConceptType a)
    {
      return addConcept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public Builder addConcept (@Nullable final ConceptPojo.Builder a)
    {
      return addConcept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addConcept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.add (a);
      return this;
    }

    @Nonnull
    public Builder concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.set (a);
      else
        m_aConcepts.clear ();
      return this;
    }

    @Nonnull
    public Builder concepts (@Nullable final ConceptPojo... a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public Builder concepts (@Nullable final Iterable <ConceptPojo> a)
    {
      m_aConcepts.setAll (a);
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
      m_aDataset = a;
      return this;
    }

    @Nonnull
    public Builder dataset (@Nullable final DCatAPDatasetType a)
    {
      return dataset (a == null ? null : DatasetPojo.builder (a));
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final RepositoryItemRefPojo.Builder a)
    {
      return repositoryItemRef (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final RepositoryItemRefPojo a)
    {
      m_aRepositoryItemRef = a;
      return this;
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final SimpleLinkType a)
    {
      return repositoryItemRef (a == null ? null : RepositoryItemRefPojo.builder (a));
    }

    public void checkConsistency ()
    {
      if (m_eQueryDefinition == null)
        throw new IllegalStateException ("Query Definition must be present");
      if (m_eResponseStatus == null)
        throw new IllegalStateException ("Response Status must be present");
      if (m_eResponseStatus != ERegRepResponseStatus.SUCCESS && m_eResponseStatus != ERegRepResponseStatus.FAILURE)
        throw new IllegalStateException ("Response Status must be SUCCESS or FAILURE");
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
          if (m_aConcepts.isEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Concept' must contain a Concept");
          if (m_aDataset != null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain a Dataset");
          break;
        case DOCUMENT:
          if (m_aConcepts.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain a Concept");
          if (m_aDataset == null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must contain a Dataset");
          break;
        case OBJECTREF:
          if (m_aConcepts.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'ObjectRef' must NOT contain a Concept");
          if (m_aDataset == null)
            throw new IllegalStateException ("A Query Definition of type 'ObjectRef' must contain a Dataset");
          if (m_aRepositoryItemRef != null)
            throw new IllegalStateException ("A Query Definition of type 'ObjectRef' must NOT contain a RepositoryItemRef");
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
                              m_aConcepts,
                              m_aDataset,
                              m_aRepositoryItemRef);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final EDMResponse.Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
      case SlotSpecificationIdentifier.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.specificationIdentifier (sValue);
        }
        break;
      case SlotIssueDateTime.NAME:
        if (aSlotValue instanceof DateTimeValueType)
        {
          final XMLGregorianCalendar aCal = ((DateTimeValueType) aSlotValue).getValue ();
          aBuilder.issueDateTime (PDTXMLConverter.getLocalDateTime (aCal));
        }
        break;
      case SlotDataProvider.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataProvider (AgentPojo.builder (new AgentMarshaller ().read (aAny)));
        }
        break;
      case SlotConceptValues.NAME:
        if (aSlotValue instanceof CollectionValueType)
        {
          final List <ValueType> aElements = ((CollectionValueType) aSlotValue).getElement ();
          if (!aElements.isEmpty ())
          {
            for (final ValueType aElement : aElements)
              if (aElement instanceof AnyValueType)
              {
                final Object aElementValue = ((AnyValueType) aElement).getAny ();
                if (aElementValue instanceof Node)
                  aBuilder.addConcept (new ConceptMarshaller ().read ((Node) aElementValue));
              }
            aBuilder.queryDefinition (EQueryDefinitionType.CONCEPT);
          }
        }
        break;
      case SlotDocumentMetadata.NAME:
      {
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataset (DatasetPojo.builder (new DatasetMarshaller ().read (aAny)));
          aBuilder.queryDefinition (EQueryDefinitionType.DOCUMENT);
        }
        break;
      }
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static EDMResponse create (@Nonnull final QueryResponse aQueryResponse) {
    final EDMResponse.Builder aBuilder = EDMResponse.builder()
            .responseStatus(ERegRepResponseStatus.getFromIDOrNull(aQueryResponse.getStatus()))
            .requestID(aQueryResponse.getRequestId());

    for (final SlotType s : aQueryResponse.getSlot())
      _applySlots(s, aBuilder);

    if (aQueryResponse.getRegistryObjectList() != null &&
            aQueryResponse.getRegistryObjectList().hasRegistryObjectEntries())
    {
      for (final SlotType aSlot : aQueryResponse.getRegistryObjectList().getRegistryObjectAtIndex(0).getSlot())
        _applySlots(aSlot, aBuilder);

      if(aQueryResponse.getRegistryObjectList().getRegistryObjectAtIndex(0) instanceof ExtrinsicObjectType)
      {
        ExtrinsicObjectType aEO = (ExtrinsicObjectType) aQueryResponse.getRegistryObjectList().getRegistryObjectAtIndex(0);

        if((aEO != null) && (aEO.getRepositoryItemRef() != null))
          aBuilder.repositoryItemRef(aEO.getRepositoryItemRef());
      }
    }

    if (aQueryResponse.getObjectRefList () != null &&
            aQueryResponse.getObjectRefList ().hasObjectRefEntries() )
    {
      for (final SlotType aSlot : aQueryResponse.getObjectRefList().getObjectRefAtIndex(0).getSlot())
        _applySlots(aSlot, aBuilder);
      aBuilder.queryDefinition (EQueryDefinitionType.OBJECTREF);
    }

    return aBuilder.build ();
  }
}
