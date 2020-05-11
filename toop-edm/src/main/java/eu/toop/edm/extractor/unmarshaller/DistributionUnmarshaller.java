package eu.toop.edm.extractor.unmarshaller;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

class DistributionUnmarshaller implements SlotUnmarshaller<DCatAPDistributionType> {
    @Override
    public DCatAPDistributionType unmarshal(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(DCatAPDistributionType.class);
        Unmarshaller um = context.createUnmarshaller();
        JAXBElement<DCatAPDistributionType> root = um.unmarshal((Node)object, DCatAPDistributionType.class);
        return root.getValue();
    }
}
