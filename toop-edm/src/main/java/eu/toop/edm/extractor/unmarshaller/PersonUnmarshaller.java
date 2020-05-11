package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.w3.cv.ac.CorePersonType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class PersonUnmarshaller implements SlotUnmarshaller<CorePersonType> {
    public CorePersonType unmarshal(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CorePersonType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<CorePersonType> root = um.unmarshal((Node)object, CorePersonType.class);
        return root.getValue();
    }
}
