package eu.toop.edm.model;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.string.StringHelper;
import eu.toop.edm.CToopEDM;
import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.creator.EDMResponseCreator;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.slot.*;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.query.QueryResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class EDMResponse {
    private final EQueryDefinitionType m_eQueryDefinition;
    private final ERegRepResponseStatus m_eResponseStatus;
    private final UUID m_aRequestID;
    private final String m_sSpecificationIdentifier;
    private final LocalDateTime m_aIssueDateTime;
    private final AgentPojo m_aDataProvider;
    private final ConceptPojo m_aConcept;
    private final DatasetPojo m_aDataset;

    public EDMResponse(EQueryDefinitionType m_eQueryDefinition,
                       ERegRepResponseStatus m_eResponseStatus,
                       UUID m_aRequestID,
                       String m_sSpecificationIdentifier,
                       LocalDateTime m_aIssueDateTime,
                       AgentPojo m_aDataProvider,
                       ConceptPojo m_aConcept,
                       DatasetPojo m_aDataset) {
        this.m_eQueryDefinition = m_eQueryDefinition;
        this.m_eResponseStatus = m_eResponseStatus;
        this.m_aRequestID = m_aRequestID;
        this.m_sSpecificationIdentifier = m_sSpecificationIdentifier;
        this.m_aIssueDateTime = m_aIssueDateTime;
        this.m_aDataProvider = m_aDataProvider;
        this.m_aConcept = m_aConcept;
        this.m_aDataset = m_aDataset;
    }

    @Nonnull
    public static Builder builder ()
    {
        // Use the default specification identifier
        return new Builder().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
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
        private UUID m_aRequestID;
        private String m_sSpecificationIdentifier;
        private LocalDateTime m_aIssueDateTime;
        private AgentPojo m_aDataProvider;
        private ConceptPojo m_aConcept;
        private DatasetPojo m_aDataset;

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
            m_aRequestID = UUID.fromString(s);
            return this;
        }

        @Nonnull
        public Builder requestID (@Nullable final UUID s)
        {
            m_aRequestID = s;
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
            if (m_aRequestID == null)
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

            return new EDMResponse( m_eQueryDefinition,
                     m_eResponseStatus,
                    m_aRequestID,
                     m_sSpecificationIdentifier,
                     m_aIssueDateTime,
                     m_aDataProvider,
                     m_aConcept,
                     m_aDataset);
        }
    }

    public EQueryDefinitionType getQueryDefinition() {
        return m_eQueryDefinition;
    }

    public ERegRepResponseStatus getResponseStatus() {
        return m_eResponseStatus;
    }

    public UUID getRequestID() {
        return m_aRequestID;
    }

    public String getSpecificationIdentifier() {
        return m_sSpecificationIdentifier;
    }

    public LocalDateTime getIssueDateTime() {
        return m_aIssueDateTime;
    }

    public AgentPojo getDataProvider() {
        return m_aDataProvider;
    }

    public ConceptPojo getConcept() {
        return m_aConcept;
    }

    public DatasetPojo getDataset() {
        return m_aDataset;
    }

    public QueryResponse getAsQueryResponse(){
        final ICommonsList<ISlotProvider> aSlots = new CommonsArrayList<>();
        if (m_sSpecificationIdentifier != null)
            aSlots.add (new SlotSpecificationIdentifier(m_sSpecificationIdentifier));
        if (m_aIssueDateTime != null)
            aSlots.add (new SlotIssueDateTime(m_aIssueDateTime));
        if (m_aDataProvider != null)
            aSlots.add (new SlotDataProvider(m_aDataProvider));

        // ConceptValues
        if (m_aConcept != null)
            aSlots.add (new SlotConceptValues(m_aConcept));

        // DocumentMetadata
        if (m_aDataset != null)
            aSlots.add (new SlotDocumentMetadata (m_aDataset));

        return new EDMResponseCreator (m_eResponseStatus, m_aRequestID.toString(), aSlots).createQueryResponse ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EDMResponse that = (EDMResponse) o;
        return m_eQueryDefinition == that.m_eQueryDefinition &&
                m_eResponseStatus == that.m_eResponseStatus &&
                Objects.equals(m_aRequestID, that.m_aRequestID) &&
                Objects.equals(m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
                Objects.equals(m_aIssueDateTime, that.m_aIssueDateTime) &&
                Objects.equals(m_aDataProvider, that.m_aDataProvider) &&
                Objects.equals(m_aConcept, that.m_aConcept) &&
                Objects.equals(m_aDataset, that.m_aDataset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_eQueryDefinition,
                m_eResponseStatus,
                m_aRequestID,
                m_sSpecificationIdentifier,
                m_aIssueDateTime,
                m_aDataProvider,
                m_aConcept,
                m_aDataset);
    }
}
