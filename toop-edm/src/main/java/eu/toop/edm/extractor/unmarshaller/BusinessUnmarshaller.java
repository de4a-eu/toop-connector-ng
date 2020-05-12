package eu.toop.edm.extractor.unmarshaller;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;

class BusinessUnmarshaller implements SlotUnmarshaller<CoreBusinessType> {
    public CoreBusinessType unmarshal(Object object) throws JAXBException {
        if (Objects.isNull(object))
            return null;
        JAXBContext context = JAXBContext.newInstance(CoreBusinessType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<CoreBusinessType> root = um.unmarshal((Node)object, CoreBusinessType.class);
        return root.getValue();
    }
}
