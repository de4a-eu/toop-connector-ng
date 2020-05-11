package eu.toop.edm.extractor.slotunmarshaller;

import eu.toop.regrep.query.QueryResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class QueryResponseUnmarshaller {

    public static QueryResponse unmarshal(File f) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(QueryResponse.class);
        Unmarshaller um = context.createUnmarshaller();
        return (QueryResponse) um.unmarshal(f);
    }

    public static QueryResponse unmarshal(InputStream is) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(QueryResponse.class);
        Unmarshaller um = context.createUnmarshaller();
        return (QueryResponse) um.unmarshal(is);
    }
}
