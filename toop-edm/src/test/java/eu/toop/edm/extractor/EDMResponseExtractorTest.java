package eu.toop.edm.extractor;

import com.helger.commons.io.resource.ClassPathResource;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Reader;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public final class EDMResponseExtractorTest {

    @Test
    public void testExtractConceptResponse() throws JAXBException {
        assertNotNull(EDMResponseExtractor.extract(RegRep4Reader.queryResponse(CCCEV.XSDS)
                .read(ClassPathResource.getInputStream("Concept Response.xml"))));
    }

    @Test
    public void testExtractDocumentResponse() throws JAXBException {
        assertNotNull(EDMResponseExtractor.extract(RegRep4Reader.queryResponse(CCCEV.XSDS)
                .read(ClassPathResource.getInputStream("Document Response.xml"))));
    }
}