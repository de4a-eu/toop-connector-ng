package eu.toop.edm.extractor.unmarshaller;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import eu.toop.edm.jaxb.cccev.CCCEVRequirementType;

public class FulFillingRequirementUnmarshaller implements SlotUnmarshaller <CCCEVRequirementType>
{
  @Override
  public CCCEVRequirementType unmarshal (Object object) throws JAXBException
  {
    if (Objects.isNull (object))
      return null;
    JAXBContext context = JAXBContext.newInstance (CCCEVRequirementType.class);
    Unmarshaller um = context.createUnmarshaller ();
    JAXBElement <CCCEVRequirementType> root = um.unmarshal ((Node) object, CCCEVRequirementType.class);
    return root.getValue ();
  }
}
