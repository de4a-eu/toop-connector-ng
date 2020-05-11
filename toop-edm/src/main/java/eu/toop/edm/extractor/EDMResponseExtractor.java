package eu.toop.edm.extractor;

import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.EDMResponseCreator;
import eu.toop.edm.extractor.slotunmarshaller.Unmarshallers;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.EDMResponse;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.*;

import javax.xml.bind.JAXBException;

final class EDMResponseExtractor {

    static EDMResponse extract(QueryResponse xmlResponse) throws JAXBException {
        EDMResponse.Builder theBuilderResponse = new EDMResponse.Builder(xmlResponse.getRequestId());

        theBuilderResponse.withResponseStatus(responseStatusExtractor(xmlResponse.getStatus()));
        if(xmlResponse.hasSlotEntries()){
            for (SlotType s : xmlResponse.getSlot()) {
                applySlots(s, theBuilderResponse);
            }
        }
        if(xmlResponse.getRegistryObjectList().hasRegistryObjectEntries()){
            for (SlotType slotType : xmlResponse.getRegistryObjectList().getRegistryObjectAtIndex(0).getSlot()) {
                applySlots(slotType, theBuilderResponse);
            }
        }

        return theBuilderResponse.build();
    }

    static QueryResponse extract(EDMResponse edmResponse) {
        switch (edmResponse.getQueryDefinition()) {
            case CONCEPT:
                return EDMResponseCreator.builderConcept()
                        .requestID(edmResponse.getRequestID().toString())
                        .responseStatus(edmResponse.getResponseStatus())
                        .issueDateTime(edmResponse.getIssueDateTime())
                        .dataProvider(edmResponse.getDataProvider())
                        .concept(edmResponse.getConceptResponse())
                        .specificationIdentifier(edmResponse.getSpecificationIdentifier())
                        .responseStatus(edmResponse.getResponseStatus())
                        .queryDefinition(edmResponse.getQueryDefinition())
                        .build();
            case DOCUMENT:
                return EDMResponseCreator.builderDocument()
                        .requestID(edmResponse.getRequestID().toString())
                        .responseStatus(edmResponse.getResponseStatus())
                        .issueDateTime(edmResponse.getIssueDateTime())
                        .dataProvider(edmResponse.getDataProvider())
                        .dataset(edmResponse.getDatasetResponse())
                        .specificationIdentifier(edmResponse.getSpecificationIdentifier())
                        .responseStatus(edmResponse.getResponseStatus())
                        .queryDefinition(edmResponse.getQueryDefinition())
                        .build();
            default:
                throw new IllegalStateException("A ConceptResponseList or a Dataset must be specified in the response.");
        }

    }

    private static ERegRepResponseStatus responseStatusExtractor(String responseStatus){
        if(responseStatus.contains("Success"))
            return ERegRepResponseStatus.SUCCESS;
        if(responseStatus.contains("PartialSuccess"))
            return ERegRepResponseStatus.PARTIAL_SUCCESS;
        if(responseStatus.contains("Failure"))
            return ERegRepResponseStatus.FAILURE;
        return null;
    }

    private static void applySlots(SlotType slotType, EDMResponse.Builder theResponse) throws JAXBException {
        if ((slotType!=null) && (slotType.getName()!=null)) {
            switch (slotType.getName()) {
                case "SpecificationIdentifier":
                    theResponse.withSpecificationIdentifier(((StringValueType) slotType.getSlotValue()).
                            getValue());
                    break;
                case "IssueDateTime":
                    theResponse.issuedAtDateTime(PDTXMLConverter.getLocalDateTime(((DateTimeValueType) slotType.getSlotValue())
                            .getValue()));
                    break;
                case "DataProvider":
                    theResponse.withDataProvider(
                            AgentPojo.builder(Unmarshallers
                                    .getAgentUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue())
                                            .getAny())).build());
                    break;
                case "ConceptValues":
                    theResponse.withConceptResponse(
                            ConceptPojo.builder(Unmarshallers
                                    .getConceptUnmarshaller()
                                    .unmarshal(((AnyValueType) ((CollectionValueType) slotType.getSlotValue())
                                            .getElementAtIndex(0))
                                            .getAny())).build());
                    break;
                case "DocumentMetadata":
                    theResponse.withDatasetResponse(DatasetPojo.builder(
                            Unmarshallers
                                    .getDatasetUnmarshaller()
                                    .unmarshal(((AnyValueType) slotType.getSlotValue())
                                            .getAny())).build());
                    break;
            }
        }
    }
}
