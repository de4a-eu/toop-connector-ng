package eu.toop.edm.extractor.unmarshaller;

import javax.xml.bind.JAXBException;

public interface SlotUnmarshaller<M> {
    M unmarshal(Object object) throws JAXBException;
}
