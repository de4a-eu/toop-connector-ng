package eu.toop.edm.extractor.slotunmarshaller;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;

public class Unmarshallers {

    public static BaseUnmarshaller<AgentType> getAgentUnmarshaller(){
        return new AgentUnmarshaller();
    }

    public static BaseUnmarshaller<CorePersonType> getPersonUnmarshaller(){
        return new PersonUnmarshaller();
    }

    public static BaseUnmarshaller<CoreBusinessType> getBusinessUnmarshaller(){
        return new BusinessUnmarshaller();
    }

    public static BaseUnmarshaller<CCCEVConceptType> getConceptUnmarshaller(){
        return new ConceptUnmarshaller();
    }

    public static BaseUnmarshaller<DCatAPDistributionType> getDistributionUnmarshaller(){
        return new DistributionUnmarshaller();
    }

    public static BaseUnmarshaller<DCatAPDatasetType> getDatasetUnmarshaller(){
        return new DatasetUnmarshaller();
    }

    private Unmarshallers(){
        //Prevent utility class instantiation
    }
}
