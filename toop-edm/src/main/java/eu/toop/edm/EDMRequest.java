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
import java.util.Locale;
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
import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.BusinessPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.model.PersonPojo;
import eu.toop.edm.slot.ISlotProvider;
import eu.toop.edm.slot.SlotAuthorizedRepresentative;
import eu.toop.edm.slot.SlotConceptRequestList;
import eu.toop.edm.slot.SlotConsentToken;
import eu.toop.edm.slot.SlotDataConsumer;
import eu.toop.edm.slot.SlotDataSubjectLegalPerson;
import eu.toop.edm.slot.SlotDataSubjectNaturalPerson;
import eu.toop.edm.slot.SlotDatasetIdentifier;
import eu.toop.edm.slot.SlotDistributionRequestList;
import eu.toop.edm.slot.SlotFullfillingRequirement;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotProcedure;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.InternationalStringType;
import eu.toop.regrep.rim.LocalizedStringType;
import eu.toop.regrep.rim.QueryType;

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
 * It is recommended to use the {@link #builder()} method to create the EDM
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
                                                                                                  SlotFullfillingRequirement.NAME,
                                                                                                  SlotConsentToken.NAME,
                                                                                                  SlotDatasetIdentifier.NAME,
                                                                                                  SlotDataConsumer.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final String m_sRequestID;
  private final String m_sSpecificationIdentifier;
  private final LocalDateTime m_aIssueDateTime;
  private final InternationalStringType m_aProcedure;
  private final CCCEVRequirementType m_aFullfillingRequirement;
  private final AgentPojo m_aDataConsumer;
  private final String m_sConsentToken;
  private final String m_sDatasetIdentifier;
  private final BusinessPojo m_aDataSubjectLegalPerson;
  private final PersonPojo m_aDataSubjectNaturalPerson;
  private final PersonPojo m_aAuthorizedRepresentative;
  private final ConceptPojo m_aConcept;
  private final DistributionPojo m_aDistribution;

  public EDMRequest (@Nonnull final EQueryDefinitionType eQueryDefinition,
                     @Nonnull @Nonempty final String sRequestID,
                     @Nonnull @Nonempty final String sSpecificationIdentifier,
                     @Nonnull final LocalDateTime aIssueDateTime,
                     @Nullable final InternationalStringType aProcedure,
                     @Nullable final CCCEVRequirementType aFullfillingRequirement,
                     @Nonnull final AgentPojo aDataConsumer,
                     @Nullable final String sConsentToken,
                     @Nullable final String sDatasetIdentifier,
                     @Nullable final BusinessPojo aDataSubjectLegalPerson,
                     @Nullable final PersonPojo aDataSubjectNaturalPerson,
                     @Nullable final PersonPojo aAuthorizedRepresentative,
                     @Nullable final ConceptPojo aConcept,
                     @Nullable final DistributionPojo aDistribution)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.notNull (aDataConsumer, "DataConsumer");
    ValueEnforcer.isFalse ((aDataSubjectLegalPerson == null && aDataSubjectNaturalPerson == null) ||
                           (aDataSubjectLegalPerson != null && aDataSubjectNaturalPerson != null),
                           "Exactly one DataSubject must be set");
    ValueEnforcer.isFalse ((aConcept == null && aDistribution == null) || (aConcept != null && aDistribution != null),
                           "Exactly one of Concept and Distribution must be set");
    switch (eQueryDefinition)
    {
      case CONCEPT:
        ValueEnforcer.notNull (aConcept, "Concept");
        break;
      case DOCUMENT:
        ValueEnforcer.notNull (aDistribution, "Distribution");
        break;
      default:
        throw new IllegalArgumentException ("Unsupported query definition: " + eQueryDefinition);
    }

    m_eQueryDefinition = eQueryDefinition;
    m_sRequestID = sRequestID;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aIssueDateTime = aIssueDateTime;
    m_aProcedure = aProcedure;
    m_aFullfillingRequirement = aFullfillingRequirement;
    m_aDataConsumer = aDataConsumer;
    m_sConsentToken = sConsentToken;
    m_sDatasetIdentifier = sDatasetIdentifier;
    m_aDataSubjectLegalPerson = aDataSubjectLegalPerson;
    m_aDataSubjectNaturalPerson = aDataSubjectNaturalPerson;
    m_aAuthorizedRepresentative = aAuthorizedRepresentative;
    m_aConcept = aConcept;
    m_aDistribution = aDistribution;
  }

  @Nonnull
  public EQueryDefinitionType getQueryDefinition ()
  {
    return m_eQueryDefinition;
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

  @Nullable
  public InternationalStringType getProcedure ()
  {
    return m_aProcedure;
  }

  @Nullable
  public CCCEVRequirementType getFullfillingRequirement ()
  {
    return m_aFullfillingRequirement;
  }

  @Nonnull
  public AgentPojo getDataConsumer ()
  {
    return m_aDataConsumer;
  }

  @Nullable
  public String getConsentToken ()
  {
    return m_sConsentToken;
  }

  @Nullable
  public String getDatasetIdentifier ()
  {
    return m_sDatasetIdentifier;
  }

  @Nullable
  public BusinessPojo getDataSubjectLegalPerson ()
  {
    return m_aDataSubjectLegalPerson;
  }

  @Nullable
  public PersonPojo getDataSubjectNaturalPerson ()
  {
    return m_aDataSubjectNaturalPerson;
  }

  @Nullable
  public PersonPojo getAuthorizedRepresentative ()
  {
    return m_aAuthorizedRepresentative;
  }

  @Nullable
  public ConceptPojo getConcept ()
  {
    return m_aConcept;
  }

  @Nullable
  public DistributionPojo getDistribution ()
  {
    return m_aDistribution;
  }

  @Nonnull
  private QueryRequest _createQueryRequest (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (m_eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notEmpty (m_sRequestID, "RequestID");
    ValueEnforcer.noNullValue (aProviders, "Providers");

    final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (m_aProviders.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      m_aProviders.put (sName, aItem);
    }

    final QueryRequest ret = RegRepHelper.createEmptyQueryRequest ();
    ret.setId (m_sRequestID);

    // All top-level slots outside of query
    for (final String sTopLevel : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = m_aProviders.get (sTopLevel);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      final QueryType aQuery = new QueryType ();
      aQuery.setQueryDefinition (m_eQueryDefinition.getID ());

      // All slots inside of query
      for (final Map.Entry <String, ISlotProvider> aEntry : m_aProviders.entrySet ())
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
    if (m_aFullfillingRequirement != null)
      aSlots.add (new SlotFullfillingRequirement (m_aFullfillingRequirement));
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
    if (m_aConcept != null)
      aSlots.add (new SlotConceptRequestList (m_aConcept));

    // Document Query
    if (m_aDistribution != null)
      aSlots.add (new SlotDistributionRequestList (m_aDistribution));

    return _createQueryRequest (aSlots);
  }

  @Nonnull
  public IVersatileWriter <QueryRequest> getWriter ()
  {
    return new JAXBVersatileWriter <> (getAsQueryRequest (),
                                       RegRep4Writer.queryRequest (CCAGV.XSDS).setFormattedOutput (true));
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
           EqualsHelper.equals (m_sRequestID, that.m_sRequestID) &&
           EqualsHelper.equals (m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
           EqualsHelper.equals (m_aIssueDateTime, that.m_aIssueDateTime) &&
           EqualsHelper.equals (m_aProcedure, that.m_aProcedure) &&
           EqualsHelper.equals (m_aFullfillingRequirement, that.m_aFullfillingRequirement) &&
           EqualsHelper.equals (m_aDataConsumer, that.m_aDataConsumer) &&
           EqualsHelper.equals (m_sConsentToken, that.m_sConsentToken) &&
           EqualsHelper.equals (m_sDatasetIdentifier, that.m_sDatasetIdentifier) &&
           EqualsHelper.equals (m_aDataSubjectLegalPerson, that.m_aDataSubjectLegalPerson) &&
           EqualsHelper.equals (m_aDataSubjectNaturalPerson, that.m_aDataSubjectNaturalPerson) &&
           EqualsHelper.equals (m_aAuthorizedRepresentative, that.m_aAuthorizedRepresentative) &&
           EqualsHelper.equals (m_aConcept, that.m_aConcept) &&
           EqualsHelper.equals (m_aDistribution, that.m_aDistribution);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eQueryDefinition)
                                       .append (m_sRequestID)
                                       .append (m_sSpecificationIdentifier)
                                       .append (m_aIssueDateTime)
                                       .append (m_aProcedure)
                                       .append (m_aFullfillingRequirement)
                                       .append (m_aDataConsumer)
                                       .append (m_sConsentToken)
                                       .append (m_sDatasetIdentifier)
                                       .append (m_aDataSubjectLegalPerson)
                                       .append (m_aDataSubjectNaturalPerson)
                                       .append (m_aAuthorizedRepresentative)
                                       .append (m_aConcept)
                                       .append (m_aDistribution)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("QueryDefinition", m_eQueryDefinition)
                                       .append ("RequestID", m_sRequestID)
                                       .append ("SpecificationIdentifier", m_sSpecificationIdentifier)
                                       .append ("IssueDateTime", m_aIssueDateTime)
                                       .append ("Procedure", m_aProcedure)
                                       .append ("FullfillingRequirement", m_aFullfillingRequirement)
                                       .append ("DataConsumer", m_aDataConsumer)
                                       .append ("ConsentToken", m_sConsentToken)
                                       .append ("DatasetIdentifier", m_sDatasetIdentifier)
                                       .append ("DataSubjectLegalPerson", m_aDataSubjectLegalPerson)
                                       .append ("DataSubjectNaturalPerson", m_aDataSubjectNaturalPerson)
                                       .append ("AuthorizedRepresentative", m_aAuthorizedRepresentative)
                                       .append ("Concept", m_aConcept)
                                       .append ("Distribution", m_aDistribution)
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

  /**
   * Builder for an EDM request
   *
   * @author Philip Helger
   */
  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private String m_sRequestID;
    private String m_sSpecificationIdentifier;
    private LocalDateTime m_aIssueDateTime;
    private InternationalStringType m_aProcedure;
    private CCCEVRequirementType m_aFullfillingRequirement;
    private AgentPojo m_aDataConsumer;
    private String m_sConsentToken;
    private String m_sDatasetIdentifier;
    private BusinessPojo m_aDataSubjectLegalPerson;
    private PersonPojo m_aDataSubjectNaturalPerson;
    private PersonPojo m_aAuthorizedRepresentative;
    private ConceptPojo m_aConcept;
    private DistributionPojo m_aDistribution;

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
    public Builder procedure (@Nonnull final Locale aLocale, @Nonnull final String sText)
    {
      return procedure (RegRepHelper.createLocalizedString (aLocale, sText));
    }

    @Nonnull
    public Builder procedure (@Nullable final Map <Locale, String> a)
    {
      return procedure (a == null ? null : RegRepHelper.createInternationalStringType (a));
    }

    @Nonnull
    public Builder procedure (@Nullable final LocalizedStringType... a)
    {
      return procedure (a == null ? null : RegRepHelper.createInternationalStringType (a));
    }

    @Nonnull
    public Builder procedure (@Nullable final InternationalStringType a)
    {
      m_aProcedure = a;
      return this;
    }

    @Nonnull
    public Builder fullfillingRequirement (@Nullable final CCCEVRequirementType a)
    {
      m_aFullfillingRequirement = a;
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
    public Builder dataConsumer (@Nullable final AgentType a)
    {
      return dataConsumer (a == null ? null : AgentPojo.builder (a));
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
    public Builder dataSubject (@Nullable final CoreBusinessType a)
    {
      return dataSubject (a == null ? null : BusinessPojo.builder (a));
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
    public Builder dataSubject (@Nullable final CorePersonType a)
    {
      return dataSubject (a == null ? null : PersonPojo.builder (a));
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
    public Builder authorizedRepresentative (@Nullable final CorePersonType a)
    {
      return authorizedRepresentative (a == null ? null : PersonPojo.builder (a));
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
    public Builder distribution (@Nullable final DistributionPojo.Builder a)
    {
      return distribution (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder distribution (@Nullable final DistributionPojo a)
    {
      m_aDistribution = a;
      return this;
    }

    @Nonnull
    public Builder distribution (@Nullable final DCatAPDistributionType a)
    {
      return distribution (a == null ? null : DistributionPojo.builder (a));
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
          if (m_aConcept == null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must contain a Concept");
          if (m_aDistribution != null)
            throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain a Distribution");
          break;
        case DOCUMENT:
          if (m_aConcept != null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain a Concept");
          if (m_aDistribution == null)
            throw new IllegalStateException ("A Query Definition of type 'Document' must contain a Distribution");
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
                             m_sSpecificationIdentifier,
                             m_aIssueDateTime,
                             m_aProcedure,
                             m_aFullfillingRequirement,
                             m_aDataConsumer,
                             m_sConsentToken,
                             m_sDatasetIdentifier,
                             m_aDataSubjectLegalPerson,
                             m_aDataSubjectNaturalPerson,
                             m_aAuthorizedRepresentative,
                             m_aConcept,
                             m_aDistribution);
    }
  }
}
