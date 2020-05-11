package eu.toop.edm.extractor.slotunmarshaller;

import eu.toop.edm.jaxb.w3.cv.ac.CoreBusinessType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class BusinessUnmarshaller implements BaseUnmarshaller<CoreBusinessType> {
    public CoreBusinessType unmarshal(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CoreBusinessType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<CoreBusinessType> root = um.unmarshal((Node)object, CoreBusinessType.class);
        return root.getValue();
    }
}
