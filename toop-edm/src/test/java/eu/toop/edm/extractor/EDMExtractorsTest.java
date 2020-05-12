package eu.toop.edm.extractor;

import com.helger.commons.io.resource.ClassPathResource;
import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import static org.junit.Assert.*;

public final class EDMExtractorsTest {

    @Test
    public void testEDMConceptRequestExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Concept Request_LP.xml")));
    }

    @Test
    public void testEDMDocumentRequestExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Document Request_NP.xml")));
    }

    @Test
    public void testEDMConceptResponseExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Concept Response.xml")));
    }

    @Test
    public void testEDMDocumentResponseExtract() throws JAXBException, XMLStreamException {
        assertNotNull(EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Document Response.xml")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Concept Request_LP.xml"));
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Concept Response.xml"));
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(ClassPathResource.getInputStream("Bogus.xml"));
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(ClassPathResource.getInputStream("Bogus.xml"));
        fail();
    }

}