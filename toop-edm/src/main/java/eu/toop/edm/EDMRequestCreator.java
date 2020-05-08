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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;

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
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.InternationalStringType;
import eu.toop.regrep.rim.LocalizedStringType;
import eu.toop.regrep.rim.QueryType;

/**
 * A simple builder to create valid TOOP Request for both "concept queries" and
 * for "document queries". See {@link #builderConcept()} and
 * {@link #builderDocument()}.
 *
 * @author Philip Helger
 */
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
  private final ICommonsOrderedMap <String, ISlotProvider> m_aProviders = new CommonsLinkedHashMap <> ();

  /**
   * Constructor
   *
   * @param eQueryDefinition
   *        Query definition type to be used. May not be <code>null</code>.
   * @param aProviders
   *        All slot providers to be added. May not be <code>null</code>.
   */
  private EDMRequestCreator (@Nonnull final EQueryDefinitionType eQueryDefinition,
                             @Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.noNullValue (aProviders, "Providers");

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
  QueryRequest createQueryRequest ()
  {
    final QueryRequest ret = RegRepHelper.createEmptyQueryRequest ();

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

    public Builder ()
    {}

    @Nonnull
    public Builder queryDefinition (@Nullable final EQueryDefinitionType e)
    {
      m_eQueryDefinition = e;
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
    public QueryRequest build ()
    {
      checkConsistency ();

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

      return new EDMRequestCreator (m_eQueryDefinition, aSlots).createQueryRequest ();
    }
  }
}
