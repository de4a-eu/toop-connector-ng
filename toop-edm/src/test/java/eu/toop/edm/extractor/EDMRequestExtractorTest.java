package eu.toop.edm.extractor;

import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Reader;

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