package eu.toop.edm.extractor;

import eu.toop.edm.creator.EDMCreators;
import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class EDMExtractorsTest {
    InputStream isConceptRequestLP,
            isConceptRequestNP,
            isDocumentRequestNP,
            isDocumentRequestLP,
            isConceptResponse,
            isDocumentResponse,
            isBogus;

    @Before
    public void setUp() {
        isConceptRequestLP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_LP.xml");
        isConceptRequestNP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_NP.xml");
        isDocumentRequestLP = this.getClass().getClassLoader().getResourceAsStream("Document Request_LP.xml");
        isDocumentRequestNP = this.getClass().getClassLoader().getResourceAsStream("Document Request_NP.xml");
        isConceptResponse = this.getClass().getClassLoader().getResourceAsStream("Concept Response.xml");
        isDocumentResponse = this.getClass().getClassLoader().getResourceAsStream("Document Response.xml");
        isBogus = this.getClass().getClassLoader().getResourceAsStream("Bogus.xml");
    }

    @Test
    public void testEDMRequestExtract() throws JAXBException,  XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.extractEDMRequest(isConceptRequestLP);
        EDMRequest conceptRequestNP = EDMExtractors.extractEDMRequest(isConceptRequestNP);
        EDMRequest documentRequestLP = EDMExtractors.extractEDMRequest(isDocumentRequestLP);
        EDMRequest documentRequestNP = EDMExtractors.extractEDMRequest(isDocumentRequestNP);

        assertNotNull(conceptRequestLP);
        assertNotNull(conceptRequestNP);
        assertNotNull(documentRequestLP);
        assertNotNull(documentRequestNP);
    }

    @Test
    public void testEDMResponseExtract() throws JAXBException,  XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.extractEDMResponse(isConceptResponse);
        EDMResponse documentResponse = EDMExtractors.extractEDMResponse(isDocumentResponse);

        assertNotNull(conceptResponse);
        assertNotNull(documentResponse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(isConceptRequestLP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(isConceptResponse);
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithRequestExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMRequest(isBogus);
        fail();
    }

    @Test(expected = XMLStreamException.class)
    public void testBogusExtractionWithResponseExtractor() throws JAXBException, XMLStreamException {
        EDMExtractors.extractEDMResponse(isBogus);
    }

}