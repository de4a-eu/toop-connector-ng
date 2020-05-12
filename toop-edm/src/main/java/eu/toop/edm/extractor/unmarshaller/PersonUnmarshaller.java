package eu.toop.edm.extractor.unmarshaller;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;

class PersonUnmarshaller implements SlotUnmarshaller <CorePersonType>
{
  public CorePersonType unmarshal (Object object) throws JAXBException
  {
    if (Objects.isNull (object))
      return null;
    JAXBContext context = JAXBContext.newInstance (CorePersonType.class);
    Unmarshaller um = context.createUnmarshaller ();
    JAXBElement <CorePersonType> root = um.unmarshal ((Node) object, CorePersonType.class);
    return root.getValue ();
  }
}
