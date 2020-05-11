package eu.toop.edm.extractor;

import com.helger.commons.string.StringHelper;
import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.extractor.unmarshaller.Unmarshallers;
import eu.toop.edm.model.*;
import eu.toop.edm.slot.*;
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

        theRequestBuilder.id(xmlRequest.getId());

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

    private static void applySlots(SlotType slotType, EDMRequest.Builder theRequestBuilder) throws JAXBException {
        if ((slotType != null) && (slotType.getName() != null) && (slotType.getSlotValue() != null)) {
            switch (slotType.getName()) {
                case SlotSpecificationIdentifier.NAME:
                    theRequestBuilder.specificationIdentifier(((StringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
                case SlotIssueDateTime.NAME:
                    theRequestBuilder.issueDateTime(PDTXMLConverter.getLocalDateTime(((DateTimeValueType) slotType.getSlotValue())
                            .getValue()));
                    break;
                case SlotProcedure.NAME:
                    theRequestBuilder.procedure(((InternationalStringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
                case SlotDataConsumer.NAME:
                    theRequestBuilder.dataConsumer(
                            AgentPojo.builder(Unmarshallers
                                    .getAgentUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case SlotConsentToken.NAME:
                    theRequestBuilder.consentToken(((StringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
                case SlotDataSubjectLegalPerson.NAME:
                    theRequestBuilder.dataSubject(
                            BusinessPojo.builder(Unmarshallers
                                    .getBusinessUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case SlotAuthorizedRepresentative.NAME:
                    theRequestBuilder.authorizedRepresentative(
                            PersonPojo.builder(Unmarshallers
                                    .getPersonUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case SlotDataSubjectNaturalPerson.NAME:
                    theRequestBuilder.dataSubject(
                            PersonPojo.builder(Unmarshallers
                                    .getPersonUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue()).getAny())).build());
                    break;
                case SlotConceptRequestList.NAME:
                    theRequestBuilder.concept(
                            ConceptPojo.builder(Unmarshallers
                                    .getConceptUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    theRequestBuilder.queryDefinition(EQueryDefinitionType.CONCEPT);
                    break;
                case SlotDistributionRequestList.NAME:
                    theRequestBuilder.distribution(DistributionPojo.builder(
                            Unmarshallers
                                    .getDistributionUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    theRequestBuilder.queryDefinition(EQueryDefinitionType.DOCUMENT);
                    break;
                case SlotDatasetIdentifier.NAME:
                    theRequestBuilder.datasetIdentifier(((StringValueType) slotType.getSlotValue())
                            .getValue());
                    break;
                case SlotFullfillingRequirement.NAME:
                    theRequestBuilder.fullfillingRequirement(
                            Unmarshallers
                                    .getCCCEVRequirementUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue())
                                            .getAny()));
                    break;
            }
        }
    }
}