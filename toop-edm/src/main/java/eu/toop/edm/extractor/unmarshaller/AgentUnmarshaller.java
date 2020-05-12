package eu.toop.edm.extractor.unmarshaller;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import eu.toop.edm.jaxb.cv.agent.AgentType;

class AgentUnmarshaller implements SlotUnmarshaller <AgentType>
{
  public AgentType unmarshal (Object object) throws JAXBException
  {
    if (Objects.isNull (object))
      return null;
    JAXBContext context = JAXBContext.newInstance (AgentType.class);
    Unmarshaller um = context.createUnmarshaller ();
    JAXBElement <AgentType> root = um.unmarshal ((Node) object, AgentType.class);
    return root.getValue ();
  }
}
