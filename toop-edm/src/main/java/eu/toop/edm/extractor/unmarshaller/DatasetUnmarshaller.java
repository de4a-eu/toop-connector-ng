package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.Objects;

class DatasetUnmarshaller implements SlotUnmarshaller<DCatAPDatasetType> {
    @Override
    public DCatAPDatasetType unmarshal(Object object) throws JAXBException {
        if (Objects.isNull(object))
            return null;
        JAXBContext context = JAXBContext.newInstance(DCatAPDatasetType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<DCatAPDatasetType> root = um.unmarshal((Node)object, DCatAPDatasetType.class);
        return root.getValue();
    }
}
