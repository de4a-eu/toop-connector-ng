package eu.toop.edm.extractor;

import eu.toop.edm.model.EDMResponse;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryResponse;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public class EDMResponseExtractorTest {

    QueryResponse queryResponse, documentResponse;

    @Before
    public void setUp() {

        queryResponse = RegRep4Reader.queryResponse ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Concept Response.xml"));
        documentResponse = RegRep4Reader.queryResponse ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Document Response.xml"));
    }

    @Test
    public void extract() throws JAXBException {
        EDMResponse x = EDMResponseExtractor.extract(queryResponse);
        EDMResponse y = EDMResponseExtractor.extract(documentResponse);

        assertNotNull(x);
        assertNotNull(y);
    }
}