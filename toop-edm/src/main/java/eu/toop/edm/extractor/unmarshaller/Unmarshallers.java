package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;

public class Unmarshallers {
    private Unmarshallers(){
        //Prevent utility class instantiation
    }

    public static SlotUnmarshaller<AgentType> getAgentUnmarshaller(){
        return new AgentUnmarshaller();
    }

    public static SlotUnmarshaller<CorePersonType> getPersonUnmarshaller(){
        return new PersonUnmarshaller();
    }

    public static SlotUnmarshaller<CoreBusinessType> getBusinessUnmarshaller(){
        return new BusinessUnmarshaller();
    }

    public static SlotUnmarshaller<CCCEVConceptType> getConceptUnmarshaller(){
        return new ConceptUnmarshaller();
    }

    public static SlotUnmarshaller<DCatAPDistributionType> getDistributionUnmarshaller(){
        return new DistributionUnmarshaller();
    }

    public static SlotUnmarshaller<DCatAPDatasetType> getDatasetUnmarshaller(){
        return new DatasetUnmarshaller();
    }

    public static SlotUnmarshaller<CCCEVRequirementType> getCCCEVRequirementUnmarshaller(){
        return new FulFillingRequirementUnmarshaller();
    }
}
