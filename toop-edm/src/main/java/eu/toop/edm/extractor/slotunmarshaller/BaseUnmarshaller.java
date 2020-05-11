package eu.toop.edm.extractor.slotunmarshaller;

import javax.xml.bind.JAXBException;

public interface BaseUnmarshaller<M> {
    M unmarshal(Object object) throws JAXBException;
}
