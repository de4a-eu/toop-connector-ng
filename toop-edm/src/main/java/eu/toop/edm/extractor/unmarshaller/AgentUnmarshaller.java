package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.cv.agent.AgentType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class AgentUnmarshaller implements SlotUnmarshaller<AgentType> {
    public AgentType unmarshal(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AgentType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<AgentType> root = um.unmarshal((Node)object, AgentType.class);
        return root.getValue();
    }
}
