package eu.toop.edm.model;

import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class EDMRequest {
    private final UUID id;
    private final LocalDateTime issueDateTime;
    private final Map<Locale, String> procedure;
    private final String consentToken;
    private final String datasetIdentifier;
    private final EQueryDefinitionType queryDefinition;
    private final String specificationIdentifier;
    private final String responseOption;

    private final AgentPojo dataConsumer;
    private final PersonPojo authorizedRepresentative;

    //Natural or Legal person
    private final PersonPojo naturalPerson;
    private final BusinessPojo legalPerson;

    private final ConceptPojo concepts;
    private final DistributionPojo distribution;


    EDMRequest(UUID id, LocalDateTime issueDateTime,
                      Map<Locale, String> procedure,
                      String consentToken,
                      String datasetIdentifier,
                      EQueryDefinitionType queryDefinition,
                      String specificationIdentifier,
                      String responseOption,
                      AgentPojo dataConsumer,
                      PersonPojo authorizedRepresentative,
                      PersonPojo naturalPerson,
                      BusinessPojo legalPerson,
                      ConceptPojo concepts,
                      DistributionPojo distribution) {
        this.id = id;
        this.issueDateTime = issueDateTime;
        this.procedure = procedure;
        this.consentToken = consentToken;
        this.datasetIdentifier = datasetIdentifier;
        this.queryDefinition = queryDefinition;
        this.specificationIdentifier = specificationIdentifier;
        this.responseOption = responseOption;
        this.dataConsumer = dataConsumer;
        this.authorizedRepresentative = authorizedRepresentative;
        this.naturalPerson = naturalPerson;
        this.legalPerson = legalPerson;
        this.concepts = concepts;
        this.distribution = distribution;
    }

    public String getResponseOption() {
        return responseOption;
    }

    public String getSpecificationIdentifier() {
        return specificationIdentifier;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public Map<Locale, String> getProcedure() {
        return procedure;
    }

    public String getConsentToken() {
        return consentToken;
    }

    public String getDatasetIdentifier() {
        return datasetIdentifier;
    }

    public EQueryDefinitionType getQueryDefinition() {
        return queryDefinition;
    }

    public AgentPojo getDataConsumer() {
        return dataConsumer;
    }

    public PersonPojo getAuthorizedRepresentative() {
        return authorizedRepresentative;
    }

    public PersonPojo getNaturalPerson() {
        return naturalPerson;
    }

    public BusinessPojo getLegalPerson() {
        return legalPerson;
    }

    public ConceptPojo getConcepts() {
        return concepts;
    }

    public DistributionPojo getDistribution() {
        return distribution;
    }

    public static class Builder {
        private UUID id;
        private LocalDateTime issueDateTime;
        private Map<Locale, String> procedure;
        private String consentToken;
        private String datasetIdentifier;
        private EQueryDefinitionType queryDefinition;
        private String specificationIdentifier;
        private String responseOption;
        private AgentPojo dataConsumer;
        private PersonPojo authorizedRepresentative;
        private PersonPojo naturalPerson;
        private BusinessPojo legalPerson;

        private ConceptPojo concepts;
        private DistributionPojo distribution;

        public Builder withID(String id) {
            Objects.requireNonNull(id);
            this.id = UUID.fromString(id);
            return this;
        }

        public Builder withID(UUID id) {
            Objects.requireNonNull(id);
            this.id = id;
            return this;
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

        public Builder withProcedure(Map<Locale, String> procedure) {
            this.procedure = procedure;
            return this;
        }

        public Builder withDataConsumer(AgentPojo dataConsumer){
            Objects.requireNonNull(dataConsumer);
            this.dataConsumer = dataConsumer;
            return this;
        }

        public Builder withConsentToken(String consentToken) {
            this.consentToken = consentToken;
            return this;
        }

        public Builder withDatasetIdentifier(String datasetIdentifier) {
            this.datasetIdentifier = datasetIdentifier;
            return this;
        }

        public Builder withSpecificationIdentifier(String specificationIdentifier) {
            Objects.requireNonNull(specificationIdentifier);
            this.specificationIdentifier = specificationIdentifier;
            return this;
        }

        public Builder withResponseOption(String responseOption) {
            this.responseOption = responseOption;
            return this;
        }

        public Builder withDataSubject(PersonPojo naturalPerson){
            Objects.requireNonNull(naturalPerson);
            if(this.legalPerson!=null)
                throw new IllegalArgumentException("The data subject can be specified as either Legal " +
                        "or Natural person but not both.");

            this.naturalPerson = naturalPerson;
            return this;
        }

        public Builder withDataSubject(BusinessPojo legalPerson){
            Objects.requireNonNull(legalPerson);
            if(this.naturalPerson!=null)
                throw new IllegalArgumentException("The data subject can be specified as either Legal " +
                        "or Natural person but not both.");

            this.legalPerson = legalPerson;
            return this;
        }

        public Builder withConcept(ConceptPojo concepts){
            Objects.requireNonNull(concepts);
            if(distribution!=null)
                throw new IllegalArgumentException("The query can either contain Concepts or" +
                        " Distribution (Document request) but NOT both.");

            this.concepts = concepts;
            this.queryDefinition = EQueryDefinitionType.CONCEPT;
            return this;
        }

        public Builder withDistribution(DistributionPojo distribution){
            Objects.requireNonNull(distribution);
            if(concepts!=null)
                throw new IllegalArgumentException("The query can either contain Concepts or" +
                        " Distribution (Document request) but NOT both.");
            this.distribution = distribution;
            this.queryDefinition = EQueryDefinitionType.DOCUMENT;
            return this;
        }

        public Builder withAuthorizedRepresentative(PersonPojo authorizedRepresentative){
            this.authorizedRepresentative = authorizedRepresentative;
            return this;
        }

        public Builder withProcedure(Locale loc, String procedure){
            this.procedure = new HashMap<>();
            this.procedure.put(loc, procedure);
            return this;
        }

        public EDMRequest build(){
            return new EDMRequest(id,
                    issueDateTime,
                    procedure,
                    consentToken,
                    datasetIdentifier,
                    queryDefinition,
                    specificationIdentifier,
                    responseOption,
                    dataConsumer,
                    authorizedRepresentative,
                    naturalPerson,
                    legalPerson,
                    concepts,
                    distribution);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EDMRequest that = (EDMRequest) o;
        return getId().equals(that.getId()) &&
                getIssueDateTime().equals(that.getIssueDateTime()) &&
                Objects.equals(getProcedure(), that.getProcedure()) &&
                Objects.equals(getConsentToken(), that.getConsentToken()) &&
                Objects.equals(getDatasetIdentifier(), that.getDatasetIdentifier()) &&
                getQueryDefinition() == that.getQueryDefinition() &&
                getSpecificationIdentifier().equals(that.getSpecificationIdentifier()) &&
                getResponseOption().equals(that.getResponseOption()) &&
                getDataConsumer().equals(that.getDataConsumer()) &&
                Objects.equals(getAuthorizedRepresentative(), that.getAuthorizedRepresentative()) &&
                Objects.equals(getNaturalPerson(), that.getNaturalPerson()) &&
                Objects.equals(getLegalPerson(), that.getLegalPerson()) &&
                Objects.equals(getConcepts(), that.getConcepts()) &&
                Objects.equals(getDistribution(), that.getDistribution());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIssueDateTime(), getProcedure(), getConsentToken(), getDatasetIdentifier(), getQueryDefinition(), getSpecificationIdentifier(), getResponseOption(), getDataConsumer(), getAuthorizedRepresentative(), getNaturalPerson(), getLegalPerson(), getConcepts(), getDistribution());
    }
}
