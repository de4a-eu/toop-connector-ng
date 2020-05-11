package eu.toop.edm.extractor;

import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.EQueryDefinitionType;
import eu.toop.edm.extractor.unmarshaller.Unmarshallers;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.EDMResponse;
import eu.toop.edm.slot.*;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.*;

import javax.xml.bind.JAXBException;

final class EDMResponseExtractor {
    private EDMResponseExtractor() {

    }

    static EDMResponse extract(QueryResponse xmlResponse) throws JAXBException {

        EDMResponse.Builder theResponseBuilder = new EDMResponse.Builder();

        theResponseBuilder.requestID(xmlResponse.getRequestId());
        theResponseBuilder.responseStatus(responseStatusExtractor(xmlResponse.getStatus()));

        if (xmlResponse.hasSlotEntries()) {
            for (SlotType s : xmlResponse.getSlot()) {
                applySlots(s, theResponseBuilder);
            }
        }
        if (xmlResponse.getRegistryObjectList().hasRegistryObjectEntries()) {
            for (SlotType slotType : xmlResponse.getRegistryObjectList().getRegistryObjectAtIndex(0).getSlot()) {
                applySlots(slotType, theResponseBuilder);
            }
        }

        return theResponseBuilder.build();
    }

    private static ERegRepResponseStatus responseStatusExtractor(String responseStatus) {
        if (responseStatus.equals(ERegRepResponseStatus.SUCCESS.getValue()))
            return ERegRepResponseStatus.SUCCESS;
        if (responseStatus.equals(ERegRepResponseStatus.PARTIAL_SUCCESS.getValue()))
            return ERegRepResponseStatus.PARTIAL_SUCCESS;
        if (responseStatus.equals(ERegRepResponseStatus.FAILURE.getValue()))
            return ERegRepResponseStatus.FAILURE;
        throw new IllegalStateException("Response status not valid: "+responseStatus);
    }

    private static void applySlots(SlotType slotType, EDMResponse.Builder theResponse) throws JAXBException {
        if ((slotType != null) && (slotType.getName() != null) && (slotType.getSlotValue() != null))
            switch (slotType.getName()) {
                case SlotSpecificationIdentifier.NAME:
                    theResponse.specificationIdentifier(((StringValueType) slotType.getSlotValue()).
                            getValue());
                    break;
                case SlotIssueDateTime.NAME:
                    theResponse.issueDateTime(PDTXMLConverter.getLocalDateTime(((DateTimeValueType) slotType.getSlotValue())
                            .getValue()));
                    break;
                case SlotDataProvider.NAME:
                    theResponse.dataProvider(
                            AgentPojo.builder(Unmarshallers
                                    .getAgentUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue())
                                            .getAny())).build());
                    break;
                case SlotConceptValues.NAME:
                    theResponse.queryDefinition(EQueryDefinitionType.CONCEPT);
                    theResponse.concept(
                            ConceptPojo.builder(Unmarshallers
                                    .getConceptUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    break;
                case SlotDocumentMetadata.NAME:
                    theResponse.dataset(DatasetPojo.builder(
                            Unmarshallers
                                    .getDatasetUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue())
                                            .getAny())).build());
                    theResponse.queryDefinition(EQueryDefinitionType.DOCUMENT);
                    break;
                default:
                    throw new IllegalStateException("Slot is not defined: " + slotType.getName());
            }
    }
}


