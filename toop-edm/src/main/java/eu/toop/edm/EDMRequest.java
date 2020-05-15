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
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

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
import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.EResponseOptionType;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.slot.SlotAuthorizedRepresentative;
import eu.toop.edm.slot.SlotConceptRequestList;
import eu.toop.edm.slot.SlotConsentToken;
import eu.toop.edm.slot.SlotDataConsumer;
import eu.toop.edm.slot.SlotDataSubjectLegalPerson;
import eu.toop.edm.slot.SlotDataSubjectNaturalPerson;
import eu.toop.edm.slot.SlotDatasetIdentifier;
import eu.toop.edm.slot.SlotDistributionRequestList;
import eu.toop.edm.slot.SlotFullfillingRequirements;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotProcedure;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileReader;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.ConceptMarshaller;
import eu.toop.edm.xml.cccev.RequirementMarshaller;
import eu.toop.edm.xml.cv.BusinessMarshaller;
import eu.toop.edm.xml.cv.PersonMarshaller;
import eu.toop.edm.xml.dcatap.DistributionMarshaller;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.CollectionValueType;
import eu.toop.regrep.rim.DateTimeValueType;
import eu.toop.regrep.rim.InternationalStringType;
import eu.toop.regrep.rim.InternationalStringValueType;
import eu.toop.regrep.rim.LocalizedStringType;
import eu.toop.regrep.rim.QueryType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;
import eu.toop.regrep.rim.ValueType;
import eu.toop.regrep.slot.ISlotProvider;
import eu.toop.regrep.slot.SlotHelper;
import eu.toop.regrep.slot.predefined.SlotId;

/**
 * This class contains the data model for a single TOOP EDM Request. It requires
 * at least the following fields:
 * <ul>
 * <li>QueryDefinition - Concept or Document query?</li>
 * <li>Request ID - the internal ID of the request that must be part of the
 * response. Can be a UUID.</li>
 * <li>Specification Identifier - must be the value
 * {@link CToopEDM#SPECIFICATION_IDENTIFIER_TOOP_EDM_V20}.</li>
 * <li>Issue date time - when the request was created. Ideally in UTC.</li>
 * <li>Data Consumer - the basic infos of the DC</li>
 * <li>Data Subject - either as Legal Person or as Natural Person, but not
 * both.</li>
 * <li>If it is a "ConceptQuery" the request Concepts must be provided.</li>
 * <li>If it is a "DocumentQuery" the request distribution must be
 * provided.</li>
 * </ul>
 * It is recommended to use the {@link #builder()} methods to create the EDM
 * request using the builder pattern with a fluent API.
 *
 * @author Philip Helger
 * @author Konstantinos Douloudis
 */
public class EDMRequest
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotProcedure.NAME,
                                                                                                  SlotFullfillingRequirements.NAME,
                                                                                                  SlotConsentToken.NAME,
                                                                                                  SlotDatasetIdentifier.NAME,
                                                                                                  SlotDataConsumer.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final String m_sRequestID;
  private final EResponseOptionType m_eResponseOption;
  private final String m_sSpecificationIdentifier;
  private final LocalDateTime m_aIssueDateTime;
  private final InternationalStringType m_aProcedure;
  private final ICommonsList <CCCEVRequirementType> m_aFullfillingRequirements = new CommonsArrayList <> ();
  private final AgentPojo m_aDataConsumer;
  private final String m_sConsentToken;
  private final String m_sDatasetIdentifier;
  private final String m_sDocumentID;
  private final BusinessPojo m_aDataSubjectLegalPerson;
  private final PersonPojo m_aDataSubjectNaturalPerson;
  private final PersonPojo m_aAuthorizedRepresentative;
  private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
  private final ICommonsList <DistributionPojo> m_aDistributions = new CommonsArrayList <> ();

  public EDMRequest (@Nonnull final EQueryDefinitionType eQueryDefinition,
                     @Nonnull @Nonempty final String sRequestID,
                     @Nonnull final EResponseOptionType eResponseOption,
                     @Nonnull @Nonempty final String sSpecificationIdentifier,
                     @Nonnull final LocalDateTime aIssueDateTime,
                     @Nullable final InternationalStringType aProcedure,
                     @Nullable final ICommonsList <CCCEVRequirementType> aFullfillingRequirements,
                     @Nonnull final AgentPojo aDataConsumer,
                     @Nullable final String sConsentToken,
                     @Nullable final String sDatasetIdentifier,
                     @Nullable final String sDocumentID,
                     @Nullable final BusinessPojo aDataSubjectLegalPerson,
                     @Nullable final PersonPojo aDataSubjectNaturalPerson,
                     @Nullable final PersonPojo aAuthorizedRepresentative,
                     @Nullable final ICommonsList <ConceptPojo> aConcepts,
                     @Nullable final ICommonsList <DistributionPojo> aDistributions)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notNull (eResponseOption, "ResponseOption");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.noNullValue (aFullfillingRequirements, "FullfillingRequirements");
    ValueEnforcer.notNull (aDataConsumer, "DataConsumer");
    ValueEnforcer.isFalse ((aDataSubjectLegalPerson == null && aDataSubjectNaturalPerson == null) ||
                           (aDataSubjectLegalPerson != null && aDataSubjectNaturalPerson != null),
                           "Exactly one DataSubject must be set");
    final int nConcepts = CollectionHelper.getSize (aConcepts);
    final int nDistributions = CollectionHelper.getSize (aDistributions);
    ValueEnforcer.isFalse (((nConcepts == 0 && nDistributions == 0) || (nConcepts != 0 && nDistributions != 0)) &&
                           (sDocumentID == null),
                           "Exactly one of Concept and Distribution or a Document ID must be set");
    switch (eQueryDefinition)
    {
      case CONCEPT:
        ValueEnforcer.notEmpty (aConcepts, "Concept");
        break;
      case DOCUMENT:
        ValueEnforcer.notEmpty (aDistributions, "Distribution");
        break;
      case OBJECTREF:
        ValueEnforcer.notEmpty (sDocumentID, "Document ID");
        break;
      default:
        throw new IllegalArgumentException ("Unsupported query definition: " + eQueryDefinition);
    }

    m_eQueryDefinition = eQueryDefinition;
    m_sRequestID = sRequestID;
    m_eResponseOption = eResponseOption;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aIssueDateTime = aIssueDateTime;
    m_aProcedure = aProcedure;
    if (aFullfillingRequirements != null)
      m_aFullfillingRequirements.addAll (aFullfillingRequirements);
    m_aDataConsumer = aDataConsumer;
    m_sConsentToken = sConsentToken;
    m_sDatasetIdentifier = sDatasetIdentifier;
    m_sDocumentID = sDocumentID;
    m_aDataSubjectLegalPerson = aDataSubjectLegalPerson;
    m_aDataSubjectNaturalPerson = aDataSubjectNaturalPerson;
    m_aAuthorizedRepresentative = aAuthorizedRepresentative;
    if (aConcepts != null)
      m_aConcepts.addAll (aConcepts);
    if (aDistributions != null)
      m_aDistributions.addAll (aDistributions);
  }

  @Nonnull
  public final EQueryDefinitionType getQueryDefinition ()
  {
    return m_eQueryDefinition;
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

  @Nullable
  public final InternationalStringType getProcedure ()
  {
    return m_aProcedure;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <CCCEVRequirementType> fullfillingRequirements ()
  {
    return m_aFullfillingRequirements;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <CCCEVRequirementType> getAllFullfillingRequirements ()
  {
    return m_aFullfillingRequirements.getClone ();
  }

  @Nonnull
  public final AgentPojo getDataConsumer ()
  {
    return m_aDataConsumer;
  }

  @Nullable
  public final String getConsentToken ()
  {
    return m_sConsentToken;
  }

  @Nullable
  public final String getDatasetIdentifier ()
  {
    return m_sDatasetIdentifier;
  }

  @Nullable
  public final String getDocumentID ()
  {
    return m_sDocumentID;
  }

  @Nullable
  public final BusinessPojo getDataSubjectLegalPerson ()
  {
    return m_aDataSubjectLegalPerson;
  }

  @Nullable
  public final PersonPojo getDataSubjectNaturalPerson ()
  {
    return m_aDataSubjectNaturalPerson;
  }

  @Nullable
  public final PersonPojo getAuthorizedRepresentative ()
  {
    return m_aAuthorizedRepresentative;
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

  @Nonnull
  @ReturnsMutableObject
  public final List <DistributionPojo> distributions ()
  {
    return m_aDistributions;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <DistributionPojo> getAllDistributions ()
  {
    return m_aDistributions.getClone ();
  }

  @Nonnull
  private QueryRequest _createQueryRequest (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (m_eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notEmpty (m_sRequestID, "RequestID");
    ValueEnforcer.noNullValue (aProviders, "Providers");

    final ICommonsOrderedMap <String, ISlotProvider> aProviderMap = new CommonsLinkedHashMap <> ();
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (aProviderMap.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      aProviderMap.put (sName, aItem);
    }

    final QueryRequest ret = RegRepHelper.createEmptyQueryRequest ();
    ret.setId (m_sRequestID);
    ret.getResponseOption ().setReturnType (m_eResponseOption.getID ());

    // All top-level slots outside of query
    for (final String sTopLevel : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = aProviderMap.get (sTopLevel);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      final QueryType aQuery = new QueryType ();
      aQuery.setQueryDefinition (m_eQueryDefinition.getID ());

      // All slots inside of query
      for (final Map.Entry <String, ISlotProvider> aEntry : aProviderMap.entrySet ())
        if (!TOP_LEVEL_SLOTS.contains (aEntry.getKey ()))
          aQuery.addSlot (aEntry.getValue ().createSlot ());

      ret.setQuery (aQuery);
    }

    return ret;
  }

  @Nonnull
  public QueryRequest getAsQueryRequest ()
  {
    final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();

    // Top-level slots
    if (m_sSpecificationIdentifier != null)
      aSlots.add (new SlotSpecificationIdentifier (m_sSpecificationIdentifier));
    if (m_aIssueDateTime != null)
      aSlots.add (new SlotIssueDateTime (m_aIssueDateTime));
    if (m_aProcedure != null)
      aSlots.add (new SlotProcedure (m_aProcedure));
    if (m_aFullfillingRequirements.isNotEmpty ())
      aSlots.add (new SlotFullfillingRequirements (m_aFullfillingRequirements));
    if (m_sConsentToken != null)
      aSlots.add (new SlotConsentToken (m_sConsentToken));
    if (m_sDatasetIdentifier != null)
      aSlots.add (new SlotDatasetIdentifier (m_sDatasetIdentifier));
    if (m_aDataConsumer != null)
      aSlots.add (new SlotDataConsumer (m_aDataConsumer));

    // Commons Query slots
    if (m_aDataSubjectLegalPerson != null)
      aSlots.add (new SlotDataSubjectLegalPerson (m_aDataSubjectLegalPerson));
    if (m_aDataSubjectNaturalPerson != null)
      aSlots.add (new SlotDataSubjectNaturalPerson (m_aDataSubjectNaturalPerson));
    if (m_aAuthorizedRepresentative != null)
      aSlots.add (new SlotAuthorizedRepresentative (m_aAuthorizedRepresentative));

    // Concept Query
    if (m_aConcepts.isNotEmpty ())
      aSlots.add (new SlotConceptRequestList (m_aConcepts));

    // Document Query
    if (m_aDistributions.isNotEmpty ())
      aSlots.add (new SlotDistributionRequestList (m_aDistributions));

    // DocumentRef Query
    if (m_sDocumentID != null)
      aSlots.add (new SlotId (m_sDocumentID));

    return _createQueryRequest (aSlots);
  }

  @Nonnull
  public IVersatileWriter <QueryRequest> getWriter ()
  {
    return new JAXBVersatileWriter <> (getAsQueryRequest (),
                                       RegRep4Writer.queryRequest (CCAGV.XSDS).setFormattedOutput (true));
  }

  @Nonnull
  public static IJAXBVersatileReader <EDMRequest> getReader ()
  {
    return new JAXBVersatileReader <> (RegRep4Reader.queryRequest (CCAGV.XSDS), EDMRequest::create);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass () != o.getClass ())
      return false;
    final EDMRequest that = (EDMRequest) o;
    return EqualsHelper.equals (m_eQueryDefinition, that.m_eQueryDefinition) &&
           EqualsHelper.equals (m_eResponseOption, that.m_eResponseOption) &&
           EqualsHelper.equals (m_sRequestID, that.m_sRequestID) &&
           EqualsHelper.equals (m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
           EqualsHelper.equals (m_aIssueDateTime, that.m_aIssueDateTime) &&
           EqualsHelper.equals (m_aProcedure, that.m_aProcedure) &&
           EqualsHelper.equals (m_aFullfillingRequirements, that.m_aFullfillingRequirements) &&
           EqualsHelper.equals (m_aDataConsumer, that.m_aDataConsumer) &&
           EqualsHelper.equals (m_sConsentToken, that.m_sConsentToken) &&
           EqualsHelper.equals (m_sDocumentID, that.m_sDocumentID) &&
           EqualsHelper.equals (m_sDatasetIdentifier, that.m_sDatasetIdentifier) &&
           EqualsHelper.equals (m_aDataSubjectLegalPerson, that.m_aDataSubjectLegalPerson) &&
           EqualsHelper.equals (m_aDataSubjectNaturalPerson, that.m_aDataSubjectNaturalPerson) &&
           EqualsHelper.equals (m_aAuthorizedRepresentative, that.m_aAuthorizedRepresentative) &&
           EqualsHelper.equals (m_aConcepts, that.m_aConcepts) &&
           EqualsHelper.equals (m_aDistributions, that.m_aDistributions);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eQueryDefinition)
                                       .append (m_eResponseOption)
                                       .append (m_sRequestID)
                                       .append (m_sSpecificationIdentifier)
                                       .append (m_aIssueDateTime)
                                       .append (m_aProcedure)
                                       .append (m_aFullfillingRequirements)
                                       .append (m_aDataConsumer)
                                       .append (m_sConsentToken)
                                       .append (m_sDatasetIdentifier)
                                       .append (m_sDocumentID)
                                       .append (m_aDataSubjectLegalPerson)
                                       .append (m_aDataSubjectNaturalPerson)
                                       .append (m_aAuthorizedRepresentative)
                                       .append (m_aConcepts)
                                       .append (m_aDistributions)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("QueryDefinition", m_eQueryDefinition)
                                       .append ("ResponseOption", m_eResponseOption)
                                       .append ("RequestID", m_sRequestID)
                                       .append ("SpecificationIdentifier", m_sSpecificationIdentifier)
                                       .append ("IssueDateTime", m_aIssueDateTime)
                                       .append ("Procedure", m_aProcedure)
                                       .append ("FullfillingRequirements", m_aFullfillingRequirements)
                                       .append ("DataConsumer", m_aDataConsumer)
                                       .append ("ConsentToken", m_sConsentToken)
                                       .append ("DatasetIdentifier", m_sDatasetIdentifier)
                                       .append ("DocumentID", m_sDocumentID)
                                       .append ("DataSubjectLegalPerson", m_aDataSubjectLegalPerson)
                                       .append ("DataSubjectNaturalPerson", m_aDataSubjectNaturalPerson)
                                       .append ("AuthorizedRepresentative", m_aAuthorizedRepresentative)
                                       .append ("Concepts", m_aConcepts)
                                       .append ("Distributions", m_aDistributions)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    // Use the default specification identifier
    return new Builder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20)
                         .responseOption (EResponseOptionType.CONTAINED);
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
    return builder ().queryDefinition (EQueryDefinitionType.DOCUMENT).responseOption (EResponseOptionType.REFERENCED);
  }

  @Nonnull
  public static Builder builderGetDocumentByID ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.OBJECTREF);
  }

  /**
   * Builder for an EDM request
   *
   * @author Philip Helger
   */
  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private String m_sRequestID;
    private EResponseOptionType m_eResponseOption;
    private String m_sSpecificationIdentifier;
    private LocalDateTime m_aIssueDateTime;
    private InternationalStringType m_aProcedure;
    private final ICommonsList <CCCEVRequirementType> m_aFullfillingRequirements = new CommonsArrayList <> ();
    private AgentPojo m_aDataConsumer;
    private String m_sConsentToken;
    private String m_sDatasetIdentifier;
    private String m_sDocumentID;
    private BusinessPojo m_aDataSubjectLegalPerson;
    private PersonPojo m_aDataSubjectNaturalPerson;
    private PersonPojo m_aAuthorizedRepresentative;
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
    private final ICommonsList <DistributionPojo> m_aDistributions = new CommonsArrayList <> ();

    protected Builder ()
    {}

    @Nonnull
    public Builder queryDefinition (@Nullable final EQueryDefinitionType e)
    {
      m_eQueryDefinition = e;
      return this;
    }

    @Nonnull
    public final Builder randomID ()
    {
      return id (UUID.randomUUID ());
    }

    @Nonnull
    public Builder responseOption (@Nullable final EResponseOptionType e)
    {
      m_eResponseOption = e;
      return this;
    }

    @Nonnull
    public final Builder id (@Nullable final UUID a)
    {
      return id (a == null ? null : a.toString ());
    }

    @Nonnull
    public final Builder id (@Nullable final String s)
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
    public Builder documentID (@Nullable final String s)
    {
      m_sDocumentID = s;
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
    public Builder procedure (@Nullable final LocalizedStringType... a)
    {
      return procedure (a == null ? null : SlotHelper.createInternationalStringType (a));
    }

    @Nonnull
    public Builder procedure (@Nonnull final Locale aLocale, @Nonnull final String sText)
    {
      return procedure (SlotHelper.createLocalizedString (aLocale, sText));
    }

    @Nonnull
    public Builder procedure (@Nullable final Map <Locale, String> a)
    {
      return procedure (a == null ? null : SlotHelper.createInternationalStringType (a));
    }

    @Nonnull
    public Builder procedure (@Nullable final InternationalStringType a)
    {
      m_aProcedure = a;
      return this;
    }

    @Nonnull
    public Builder addFullfillingRequirement (@Nullable final CCCEVRequirementType a)
    {
      if (a != null)
        m_aFullfillingRequirements.add (a);
      return this;
    }

    @Nonnull
    public Builder fullfillingRequirement (@Nullable final CCCEVRequirementType a)
    {
      if (a != null)
        m_aFullfillingRequirements.set (a);
      else
        m_aFullfillingRequirements.clear ();
      return this;
    }

    @Nonnull
    public Builder fullfillingRequirements (@Nullable final CCCEVRequirementType... a)
    {
      m_aFullfillingRequirements.setAll (a);
      return this;
    }

    @Nonnull
    public Builder fullfillingRequirements (@Nullable final Iterable <? extends CCCEVRequirementType> a)
    {
      m_aFullfillingRequirements.setAll (a);
      return this;
    }

    @Nonnull
    public Builder consentToken (@Nullable final String s)
    {
      m_sConsentToken = s;
      return this;
    }

    @Nonnull
    public Builder datasetIdentifier (@Nullable final String s)
    {
      m_sDatasetIdentifier = s;
      return this;
    }

    @Nonnull
    public Builder dataConsumer (@Nullable final AgentType a)
    {
      return dataConsumer (a == null ? null : AgentPojo.builder (a));
    }

    @Nonnull
    public Builder dataConsumer (@Nullable final AgentPojo.Builder a)
    {
      return dataConsumer (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataConsumer (@Nullable final AgentPojo a)
    {
      m_aDataConsumer = a;
      return this;
    }

    @Nonnull
    public Builder dataSubject (@Nullable final CoreBusinessType a)
    {
      return dataSubject (a == null ? null : BusinessPojo.builder (a));
    }

    @Nonnull
    public Builder dataSubject (@Nullable final BusinessPojo.Builder a)
    {
      return dataSubject (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataSubject (@Nullable final BusinessPojo a)
    {
      m_aDataSubjectLegalPerson = a;
      m_aDataSubjectNaturalPerson = null;
      return this;
    }

    @Nonnull
    public Builder dataSubject (@Nullable final CorePersonType a)
    {
      return dataSubject (a == null ? null : PersonPojo.builder (a));
    }

    @Nonnull
    public Builder dataSubject (@Nullable final PersonPojo.Builder a)
    {
      return dataSubject (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataSubject (@Nullable final PersonPojo a)
    {
      m_aDataSubjectLegalPerson = null;
      m_aDataSubjectNaturalPerson = a;
      return this;
    }

    @Nonnull
    public Builder authorizedRepresentative (@Nullable final CorePersonType a)
    {
      return authorizedRepresentative (a == null ? null : PersonPojo.builder (a));
    }

    @Nonnull
    public Builder authorizedRepresentative (@Nullable final PersonPojo.Builder a)
    {
      return authorizedRepresentative (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder authorizedRepresentative (@Nullable final PersonPojo a)
    {
      m_aAuthorizedRepresentative = a;
      return this;
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
    public Builder addDistribution (@Nullable final DCatAPDistributionType a)
    {
      return addDistribution (a == null ? null : DistributionPojo.builder (a));
    }

    @Nonnull
    public Builder addDistribution (@Nullable final DistributionPojo.Builder a)
    {
      return addDistribution (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addDistribution (@Nullable final DistributionPojo a)
    {
      if (a != null)
        m_aDistributions.add (a);
      return this;
    }

    @Nonnull
    public Builder distribution (@Nullable final DCatAPDistributionType a)
    {
      return distribution (a == null ? null : DistributionPojo.builder (a));
    }

    @Nonnull
    public Builder distribution (@Nullable final DistributionPojo.Builder a)
    {
      return distribution (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder distribution (@Nullable final DistributionPojo a)
    {
      if (a != null)
        m_aDistributions.set (a);
      else
        m_aDistributions.clear ();
      return this;
    }

    @Nonnull
    public Builder distributions (@Nullable final DistributionPojo... a)
    {
      m_aDistributions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder distributions (@Nullable final Iterable <DistributionPojo> a)
    {
      m_aDistributions.setAll (a);
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eQueryDefinition == null)
        throw new IllegalStateException ("Query Definition must be present");
      if (StringHelper.hasNoText (m_sRequestID))
        throw new IllegalStateException ("ID must be present");
      if (StringHelper.hasNoText (m_sSpecificationIdentifier))
        throw new IllegalStateException ("SpecificationIdentifier must be present");
      if (m_aIssueDateTime == null)
        throw new IllegalStateException ("Issue Date Time must be present");
      if (m_aDataConsumer == null)
        throw new IllegalStateException ("Cata Consumer must be present");

      if (m_aDataSubjectLegalPerson == null && m_aDataSubjectNaturalPerson == null)
        throw new IllegalStateException ("Data Subject must be present");
      if (m_aDataSubjectLegalPerson != null && m_aDataSubjectNaturalPerson != null)
        throw new IllegalStateException ("Data Subject MUST be either legal person OR natural person");

      switch (m_eQueryDefinition)
      {
        case CONCEPT:
          if (m_aConcepts.isEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Concept' must contain a Concept");
          if (m_aDistributions.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain a Distribution");
          if (m_sDocumentID != null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain a Document ID");
          break;
        case DOCUMENT:
          if (m_aConcepts.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain a Concept");
          if (m_aDistributions.isEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'Document' must contain a Distribution");
          if (m_sDocumentID != null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain a Document ID");
          break;
        case OBJECTREF:
          if (m_aConcepts.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'GetObjectByID' must NOT contain a Concept");
          if (m_aDistributions.isNotEmpty ())
            throw new IllegalStateException ("A Query Definition of type 'GetObjectByID' must NOT contain a Distribution");
          if (m_sDocumentID == null)
            throw new IllegalStateException ("A Query Definition of type 'GetObjectByID' must contain a Document ID");
          break;
        default:
          throw new IllegalStateException ("Unhandled query definition " + m_eQueryDefinition);
      }
    }

    @Nonnull
    public EDMRequest build ()
    {
      checkConsistency ();

      return new EDMRequest (m_eQueryDefinition,
                             m_sRequestID,
                             m_eResponseOption,
                             m_sSpecificationIdentifier,
                             m_aIssueDateTime,
                             m_aProcedure,
                             m_aFullfillingRequirements,
                             m_aDataConsumer,
                             m_sConsentToken,
                             m_sDatasetIdentifier,
                             m_sDocumentID,
                             m_aDataSubjectLegalPerson,
                             m_aDataSubjectNaturalPerson,
                             m_aAuthorizedRepresentative,
                             m_aConcepts,
                             m_aDistributions);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final EDMRequest.Builder aBuilder)
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
      case SlotProcedure.NAME:
        if (aSlotValue instanceof InternationalStringValueType)
        {
          final InternationalStringType aIntString = ((InternationalStringValueType) aSlotValue).getValue ();
          aBuilder.procedure (aIntString);
        }
        break;
      case SlotFullfillingRequirements.NAME:
        if (aSlotValue instanceof CollectionValueType)
        {
          final List <ValueType> aElements = ((CollectionValueType) aSlotValue).getElement ();
          for (final ValueType aElement : aElements)
            if (aElement instanceof AnyValueType)
            {
              final Object aElementValue = ((AnyValueType) aElement).getAny ();
              if (aElementValue instanceof Node)
                aBuilder.addFullfillingRequirement (new RequirementMarshaller ().read ((Node) aElementValue));
            }
        }
        break;
      case SlotConsentToken.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.consentToken (sValue);
        }
        break;
      case SlotDatasetIdentifier.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.datasetIdentifier (sValue);
        }
        break;
      case SlotId.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.documentID (sValue);
          aBuilder.queryDefinition (EQueryDefinitionType.OBJECTREF);
        }
        break;
      case SlotDataConsumer.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataConsumer (AgentPojo.builder (new AgentMarshaller ().read (aAny)));
        }
        break;
      case SlotDataSubjectLegalPerson.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataSubject (BusinessPojo.builder (new BusinessMarshaller ().read (aAny)));
        }
        break;
      case SlotDataSubjectNaturalPerson.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataSubject (PersonPojo.builder (new PersonMarshaller ().read (aAny)));
        }
        break;
      case SlotAuthorizedRepresentative.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.authorizedRepresentative (PersonPojo.builder (new PersonMarshaller ().read (aAny)));
        }
        break;
      case SlotConceptRequestList.NAME:
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
      case SlotDistributionRequestList.NAME:
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
                  aBuilder.addDistribution (new DistributionMarshaller ().read ((Node) aElementValue));
              }
            aBuilder.queryDefinition (EQueryDefinitionType.DOCUMENT);
          }
        }
        break;
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static EDMRequest create (@Nonnull final QueryRequest aQueryRequest)
  {
    final EDMRequest.Builder aBuilder = EDMRequest.builder ().id (aQueryRequest.getId ());

    for (final SlotType slot : aQueryRequest.getSlot ())
      _applySlots (slot, aBuilder);

    if (aQueryRequest.getQuery () != null && aQueryRequest.getQuery ().hasSlotEntries ())
      for (final SlotType aSlot : aQueryRequest.getQuery ().getSlot ())
        if (aSlot != null)
          _applySlots (aSlot, aBuilder);

    if (aQueryRequest.getResponseOption () != null && aQueryRequest.getResponseOption ().getReturnType () != null)
      aBuilder.responseOption (EResponseOptionType.getFromIDOrNull (aQueryRequest.getResponseOption ()
                                                                                 .getReturnType ()));

    return aBuilder.build ();
  }
}
