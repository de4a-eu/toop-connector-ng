package eu.toop.edm.extractor;

import eu.toop.edm.model.EDMRequest;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryRequest;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public class EDMRequestExtractorTest {

    QueryRequest queryRequestLP, queryRequestNP, documentRequestNP, documentRequestLP;

    @Before
    public void setUp() {
        queryRequestLP = RegRep4Reader.queryRequest ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Concept Request_LP.xml"));
        queryRequestNP = RegRep4Reader.queryRequest ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Concept Request_NP.xml"));
        documentRequestLP = RegRep4Reader.queryRequest ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Document Request_LP.xml"));
        documentRequestNP = RegRep4Reader.queryRequest ()
                .read(this.getClass().getClassLoader().getResourceAsStream("Document Request_NP.xml"));

    }

    @Test
    public void extract() throws JAXBException {
        EDMRequest w = EDMRequestExtractor.extract(queryRequestLP);
        EDMRequest x = EDMRequestExtractor.extract(queryRequestNP);
        EDMRequest y = EDMRequestExtractor.extract(documentRequestLP);
        EDMRequest z = EDMRequestExtractor.extract(documentRequestNP);

        assertNotNull(w);
        assertNotNull(x);
        assertNotNull(y);
        assertNotNull(z);
    }
}