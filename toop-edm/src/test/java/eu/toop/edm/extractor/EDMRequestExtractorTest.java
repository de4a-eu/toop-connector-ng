package eu.toop.edm.extractor;

import com.helger.commons.io.resource.ClassPathResource;
import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.query.QueryRequest;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public final class EDMRequestExtractorTest {

    @Test
    public void testConceptRequestLPExtractor() throws JAXBException {
        assertNotNull(EDMRequestExtractor.extract(RegRep4Reader.queryRequest(CCAGV.XSDS)
                .read(ClassPathResource.getInputStream("Concept Request_LP.xml"))));
    }

    @Test
    public void testConceptResponseNPExtractor() throws JAXBException {
        assertNotNull(EDMRequestExtractor.extract(RegRep4Reader.queryRequest(CCAGV.XSDS)
                .read(ClassPathResource.getInputStream("Concept Request_NP.xml"))));
    }

    @Test
    public void testDocumentRequestLPExtractor() throws JAXBException {
        assertNotNull(EDMRequestExtractor.extract(RegRep4Reader.queryRequest(CCAGV.XSDS)
                .read(ClassPathResource.getInputStream("Document Request_LP.xml"))));
    }

    @Test
    public void testDocumentRequestNPExtractor() throws JAXBException {
        assertNotNull(EDMRequestExtractor.extract(RegRep4Reader.queryRequest(CCAGV.XSDS)
                .read(ClassPathResource.getInputStream("Document Request_NP.xml"))));
    }
}