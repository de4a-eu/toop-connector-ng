package eu.toop.edm.model;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.CToopEDM;
import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.creator.EDMRequestCreator;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
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
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.InternationalStringType;
import eu.toop.regrep.rim.LocalizedStringType;

public class EDMRequest {

    private final UUID m_aId;
    private final EQueryDefinitionType m_eQueryDefinition;
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

    public EDMRequest(UUID m_aId,
                      EQueryDefinitionType m_eQueryDefinition,
                      String m_sSpecificationIdentifier,
                      LocalDateTime m_aIssueDateTime,
                      InternationalStringType m_aProcedure,
                      CCCEVRequirementType m_aFullfillingRequirement,
                      AgentPojo m_aDataConsumer,
                      String m_sConsentToken,
                      String m_sDatasetIdentifier,
                      BusinessPojo m_aDataSubjectLegalPerson,
                      PersonPojo m_aDataSubjectNaturalPerson,
                      PersonPojo m_aAuthorizedRepresentative,
                      ConceptPojo m_aConcept,
                      DistributionPojo m_aDistribution) {
        this.m_aId = m_aId;
        this.m_eQueryDefinition = m_eQueryDefinition;
        this.m_sSpecificationIdentifier = m_sSpecificationIdentifier;
        this.m_aIssueDateTime = m_aIssueDateTime;
        this.m_aProcedure = m_aProcedure;
        this.m_aFullfillingRequirement = m_aFullfillingRequirement;
        this.m_aDataConsumer = m_aDataConsumer;
        this.m_sConsentToken = m_sConsentToken;
        this.m_sDatasetIdentifier = m_sDatasetIdentifier;
        this.m_aDataSubjectLegalPerson = m_aDataSubjectLegalPerson;
        this.m_aDataSubjectNaturalPerson = m_aDataSubjectNaturalPerson;
        this.m_aAuthorizedRepresentative = m_aAuthorizedRepresentative;
        this.m_aConcept = m_aConcept;
        this.m_aDistribution = m_aDistribution;
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

        private UUID m_aId;
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
        public Builder id(@Nonnull final UUID id){
            m_aId = id;
            return this;
        }

        @Nonnull
        public Builder id(@Nonnull final String id){
            m_aId = UUID.fromString(id);
            return this;
        }

        @Nonnull
        public Builder randomID(){
            m_aId = UUID.randomUUID();
            return this;
        }

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
            if (m_aId == null)
                throw new IllegalStateException ("ID must be present");
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

        @Nonnull public EDMRequest build(){
            checkConsistency();

            return  new EDMRequest(m_aId,
                     m_eQueryDefinition,
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

    public UUID getId() {
        return m_aId;
    }

    public EQueryDefinitionType getQueryDefinition() {
        return m_eQueryDefinition;
    }

    public String getSpecificationIdentifier() {
        return m_sSpecificationIdentifier;
    }

    public LocalDateTime getIssueDateTime() {
        return m_aIssueDateTime;
    }

    public InternationalStringType getProcedure() {
        return m_aProcedure;
    }

    public CCCEVRequirementType getFullfillingRequirement() {
        return m_aFullfillingRequirement;
    }

    public AgentPojo getDataConsumer() {
        return m_aDataConsumer;
    }

    public String getConsentToken() {
        return m_sConsentToken;
    }

    public String getDatasetIdentifier() {
        return m_sDatasetIdentifier;
    }

    public BusinessPojo getDataSubjectLegalPerson() {
        return m_aDataSubjectLegalPerson;
    }

    public PersonPojo getDataSubjectNaturalPerson() {
        return m_aDataSubjectNaturalPerson;
    }

    public PersonPojo getAuthorizedRepresentative() {
        return m_aAuthorizedRepresentative;
    }

    public ConceptPojo getConcept() {
        return m_aConcept;
    }

    public DistributionPojo getDistribution() {
        return m_aDistribution;
    }

    public QueryRequest getAsQueryRequest(){

        final ICommonsList<ISlotProvider> aSlots = new CommonsArrayList<>();

        // Top-level slots
        if (m_sSpecificationIdentifier != null)
            aSlots.add (new SlotSpecificationIdentifier(m_sSpecificationIdentifier));
        if (m_aIssueDateTime != null)
            aSlots.add (new SlotIssueDateTime(m_aIssueDateTime));
        if (m_aProcedure != null)
            aSlots.add (new SlotProcedure(m_aProcedure));
        if (m_aFullfillingRequirement != null)
            aSlots.add (new SlotFullfillingRequirement(m_aFullfillingRequirement));
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

        return new EDMRequestCreator(m_eQueryDefinition, m_aId.toString(), aSlots).createQueryRequest ();
    }

    public String getAsXMLString(){
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsString(getAsQueryRequest());
    }

    public InputStream getAsXMLInputStream(){
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsInputStream(getAsQueryRequest());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EDMRequest that = (EDMRequest) o;
        return EqualsHelper.equals(m_aId, that.m_aId) &&
                EqualsHelper.equals(m_eQueryDefinition , that.m_eQueryDefinition ) &&
                EqualsHelper.equals(m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
                EqualsHelper.equals(m_aIssueDateTime, that.m_aIssueDateTime) &&
                EqualsHelper.equals(m_aProcedure, that.m_aProcedure) &&
                EqualsHelper.equals(m_aFullfillingRequirement, that.m_aFullfillingRequirement) &&
                EqualsHelper.equals(m_aDataConsumer, that.m_aDataConsumer) &&
                EqualsHelper.equals(m_sConsentToken, that.m_sConsentToken) &&
                EqualsHelper.equals(m_sDatasetIdentifier, that.m_sDatasetIdentifier) &&
                EqualsHelper.equals(m_aDataSubjectLegalPerson, that.m_aDataSubjectLegalPerson) &&
                EqualsHelper.equals(m_aDataSubjectNaturalPerson, that.m_aDataSubjectNaturalPerson) &&
                EqualsHelper.equals(m_aAuthorizedRepresentative, that.m_aAuthorizedRepresentative) &&
                EqualsHelper.equals(m_aConcept, that.m_aConcept) &&
                EqualsHelper.equals(m_aDistribution, that.m_aDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_aId,
                m_eQueryDefinition,
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

    @Override
    public String toString() {
        return new ToStringGenerator(this)
                .append("ID", m_aId)
                .append("QueryDefinition", m_eQueryDefinition)
                .append("SpecificationIdentifier", m_sSpecificationIdentifier)
                .append("IssueDateTime", m_aIssueDateTime)
                .append("Procedure", m_aProcedure)
                .append("FullfillingRequirement", m_aFullfillingRequirement)
                .append("DataConsumer", m_aDataConsumer)
                .append("ConsentToken", m_sConsentToken)
                .append("DatasetIdentifier", m_sDatasetIdentifier)
                .append("DataSubjectLegalPerson", m_aDataSubjectLegalPerson)
                .append("DataSubjectNaturalPerson", m_aDataSubjectNaturalPerson)
                .append("AuthorizedRepresentative", m_aAuthorizedRepresentative)
                .append("Concept", m_aConcept)
                .append("Distribution", m_aDistribution)
                .getToString();
    }
}
