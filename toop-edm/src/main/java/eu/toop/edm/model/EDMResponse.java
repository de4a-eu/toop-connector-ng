package eu.toop.edm.model;

import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.regrep.ERegRepResponseStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class EDMResponse {
    private final UUID requestID;
    private final ERegRepResponseStatus responseStatus;
    private final LocalDateTime issueDateTime;
    private final String specificationIdentifier;
    private final EQueryDefinitionType queryDefinition;

    private final AgentPojo dataProvider;
    private final ConceptPojo conceptResponse;
    private final DatasetPojo datasetResponse;

    EDMResponse(UUID requestID,
                       ERegRepResponseStatus responseStatus,
                       LocalDateTime issueDateTime,
                       String specificationIdentifier,
                       EQueryDefinitionType queryDefinition,
                       AgentPojo dataProvider,
                       ConceptPojo conceptResponse,
                       DatasetPojo datasetResponse) {

        this.requestID = requestID;
        this.responseStatus = responseStatus;
        this.issueDateTime = issueDateTime;
        this.specificationIdentifier = specificationIdentifier;
        this.queryDefinition = queryDefinition;
        this.dataProvider = dataProvider;
        this.conceptResponse = conceptResponse;
        this.datasetResponse = datasetResponse;
    }

    public UUID getRequestID() {
        return requestID;
    }

    public ERegRepResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public String getSpecificationIdentifier() {
        return specificationIdentifier;
    }

    public EQueryDefinitionType getQueryDefinition() {
        return queryDefinition;
    }

    public AgentPojo getDataProvider() {
        return dataProvider;
    }

    public ConceptPojo getConceptResponse() {
        return conceptResponse;
    }

    public DatasetPojo getDatasetResponse() {
        return datasetResponse;
    }

    public static class Builder{
        private final UUID requestID;
        private ERegRepResponseStatus responseStatus;
        private LocalDateTime issueDateTime;
        private String specificationIdentifier;
        private EQueryDefinitionType queryDefinition;

        private AgentPojo dataProvider;
        private ConceptPojo conceptResponse;
        private DatasetPojo datasetResponse;

        public Builder(String requestID){
            Objects.requireNonNull(requestID);
            this.requestID = UUID.fromString(requestID);
        }

        public Builder(UUID requestID){
            this.requestID = requestID;
        }

        public Builder issuedAtDateTime(LocalDateTime issueDateTime) {
            Objects.requireNonNull(issueDateTime);
            this.issueDateTime = issueDateTime;
            return this;
        }

        public Builder issuedNow() {
            this.issueDateTime = LocalDateTime.now();
            return this;
        }

        public Builder withSpecificationIdentifier(String specificationIdentifier) {
            Objects.requireNonNull(specificationIdentifier);
            this.specificationIdentifier = specificationIdentifier;
            return this;
        }

        public Builder withResponseStatus(ERegRepResponseStatus responseStatus){
            Objects.requireNonNull(responseStatus);
            this.responseStatus = responseStatus;
            return this;
        }

        public Builder withDataProvider(AgentPojo dataProvider){
            Objects.requireNonNull(dataProvider);
            this.dataProvider = dataProvider;
            return this;
        }

        public Builder withConceptResponse(ConceptPojo conceptResponse){
            Objects.requireNonNull(conceptResponse);
            if(datasetResponse != null)
                throw new IllegalArgumentException("A response may contain either ConceptResponses " +
                        "or Dataset but not both.");
            this.conceptResponse = conceptResponse;
            this.queryDefinition = EQueryDefinitionType.CONCEPT;
            return this;
        }

        public Builder withDatasetResponse(DatasetPojo datasetResponse){
            Objects.requireNonNull(datasetResponse);
            if(conceptResponse != null)
                throw new IllegalArgumentException("A response may contain either ConceptResponses " +
                        "or Dataset but not both.");
            this.datasetResponse = datasetResponse;
            this.queryDefinition = EQueryDefinitionType.DOCUMENT;
            return this;
        }

        public EDMResponse build(){
            return new EDMResponse(requestID,
                    responseStatus,
                    issueDateTime,
                    specificationIdentifier,
                    queryDefinition,
                    dataProvider,
                    conceptResponse,
                    datasetResponse);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EDMResponse that = (EDMResponse) o;
        return getRequestID().equals(that.getRequestID()) &&
                getResponseStatus() == that.getResponseStatus() &&
                getIssueDateTime().equals(that.getIssueDateTime()) &&
                Objects.equals(getSpecificationIdentifier(), that.getSpecificationIdentifier()) &&
                getQueryDefinition() == that.getQueryDefinition() &&
                getDataProvider().equals(that.getDataProvider()) &&
                Objects.equals(getConceptResponse(), that.getConceptResponse()) &&
                Objects.equals(getDatasetResponse(), that.getDatasetResponse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequestID(), getResponseStatus(), getIssueDateTime(), getSpecificationIdentifier(), getQueryDefinition(), getDataProvider(), getConceptResponse(), getDatasetResponse());
    }
}
