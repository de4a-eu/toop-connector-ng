package eu.toop.edm.extractor;

import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.EDMRequestCreator;
import eu.toop.edm.extractor.slotunmarshaller.Unmarshallers;
import eu.toop.edm.model.*;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.rim.*;


import javax.xml.bind.JAXBException;
import java.util.Locale;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

final class EDMRequestExtractor {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(EDMRequestExtractor.class.getName());

    static EDMRequest extract(QueryRequest xmlRequest) throws JAXBException {
        EDMRequest.Builder theRequestBuilder = new EDMRequest.Builder();

        theRequestBuilder.withID(xmlRequest.getId());
        theRequestBuilder.withResponseOption(xmlRequest.getResponseOption().getReturnType());

        if (xmlRequest.hasSlotEntries()) {
            for (SlotType slot : xmlRequest.getSlot()) {
                applySlots(slot, theRequestBuilder);
            }
        }
        if (xmlRequest.getQuery().hasSlotEntries()) {
            for (SlotType slot : xmlRequest.getQuery().getSlot()) {
                applySlots(slot, theRequestBuilder);
            }
        }

        return theRequestBuilder.build();
    }

    static QueryRequest extract(EDMRequest edmRequest) {
        sanityCheck(edmRequest);
        EDMRequestCreator.Builder builder;
        switch (edmRequest.getQueryDefinition()){
            case CONCEPT:
                return EDMRequestCreator.builderConcept()
                        .specificationIdentifier(edmRequest.getSpecificationIdentifier())
                        .issueDateTime(edmRequest.getIssueDateTime())
                        .procedure(edmRequest.getProcedure())
                        .fullfillingRequirement(null)
                        .consentToken(edmRequest.getConsentToken())
                        .datasetIdentifier(edmRequest.getDatasetIdentifier())
                        .dataConsumer(edmRequest.getDataConsumer())
                        .authorizedRepresentative(edmRequest.getAuthorizedRepresentative())
                        .dataSubject(edmRequest.getNaturalPerson())
                        .dataSubject(edmRequest.getLegalPerson())
                        .queryDefinition(edmRequest.getQueryDefinition())
                        .concept(edmRequest.getConcepts())
                        .build();
            case DOCUMENT:
                return EDMRequestCreator.builderDocument()
                        .specificationIdentifier(edmRequest.getSpecificationIdentifier())
                        .issueDateTime(edmRequest.getIssueDateTime())
                        .procedure(edmRequest.getProcedure())
                        .fullfillingRequirement(null)
                        .consentToken(edmRequest.getConsentToken())
                        .datasetIdentifier(edmRequest.getDatasetIdentifier())
                        .dataConsumer(edmRequest.getDataConsumer())
                        .authorizedRepresentative(edmRequest.getAuthorizedRepresentative())
                        .dataSubject(edmRequest.getNaturalPerson())
                        .dataSubject(edmRequest.getLegalPerson())
                        .queryDefinition(edmRequest.getQueryDefinition())
                        .distribution(edmRequest.getDistribution())
                        .build();
            default:
                throw new IllegalStateException("QueryDefinition must be specified as either CONCEPT or QUERY.");
        }
    }

    private static void sanityCheck(EDMRequest edmRequest){
        if ((edmRequest.getLegalPerson() != null) && (edmRequest.getNaturalPerson() != null))
            throw new IllegalStateException("A request can either contain a Legal or a Natural person but not both.");
        if((edmRequest.getDistribution() != null) && (edmRequest.getConcepts() != null))
            throw new IllegalStateException("A request may either contain Concepts or Distribution but not both.");
    }

    private static void applySlots(SlotType slotType, EDMRequest.Builder theRequestBuilder) throws JAXBException {
        if ((slotType != null) && (slotType.getName() != null) && (slotType.getSlotValue() != null)) {
            switch (slotType.getName()) {
                case "SpecificationIdentifier":
                    theRequestBuilder.withSpecificationIdentifier(((StringValueType) slotType.getSlotValue()).
                            getValue());
                    break;
                case "IssueDateTime":
                    theRequestBuilder.issuedAtDateTime(PDTXMLConverter.getLocalDateTime(((DateTimeValueType) slotType.getSlotValue())
                            .getValue()));
                    break;
                case "Procedure":
                    theRequestBuilder.withProcedure(((InternationalStringValueType) slotType.getSlotValue())
                            .getValue()
                            .getLocalizedString()
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            lst -> Locale.forLanguageTag(lst.getLang()),
                                            LocalizedStringType::getValue)));
                    break;
                case "DataConsumer":
                    theRequestBuilder.withDataConsumer(
                            AgentPojo.builder(Unmarshallers
                                    .getAgentUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case "ConsentToken":
                    theRequestBuilder.withConsentToken(((StringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
                case "LegalPerson":
                    theRequestBuilder.withDataSubject(
                            BusinessPojo.builder(Unmarshallers
                                    .getBusinessUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case "AuthorizedRepresentative":
                    theRequestBuilder.withAuthorizedRepresentative(
                            PersonPojo.builder(Unmarshallers
                                    .getPersonUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case "NaturalPerson":
                    theRequestBuilder.withDataSubject(
                            PersonPojo.builder(Unmarshallers
                                    .getPersonUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case "ConceptRequestList":
                    theRequestBuilder.withConcept(
                            ConceptPojo.builder(Unmarshallers
                                    .getConceptUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    break;
                case "DistributionRequestList":
                    theRequestBuilder.withDistribution(DistributionPojo.builder(
                            Unmarshallers
                                    .getDistributionUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    break;
                case "DatasetIdentifier":
                    theRequestBuilder.withDatasetIdentifier(((StringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
            }
        }
    }
}