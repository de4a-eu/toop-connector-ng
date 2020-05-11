package eu.toop.edm.extractor.slotunmarshaller;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class ConceptUnmarshaller implements BaseUnmarshaller<CCCEVConceptType> {
    public CCCEVConceptType unmarshal(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CCCEVConceptType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<CCCEVConceptType> root = um.unmarshal((Node)object, CCCEVConceptType.class);
        return root.getValue();
    }
}
