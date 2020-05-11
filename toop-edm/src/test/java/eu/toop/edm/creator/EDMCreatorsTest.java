package eu.toop.edm.creator;

import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class EDMCreatorsTest {
    InputStream isConceptRequestLP, isConceptRequestNP, isDocumentRequestNP, isDocumentRequestLP, isConceptResponse, isDocumentResponse;

    @Before
    public void setUp() {
        isConceptRequestLP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_LP.xml");
        isConceptRequestNP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_NP.xml");
        isDocumentRequestLP = this.getClass().getClassLoader().getResourceAsStream("Document Request_LP.xml");
        isDocumentRequestNP = this.getClass().getClassLoader().getResourceAsStream("Document Request_NP.xml");
        isConceptResponse = this.getClass().getClassLoader().getResourceAsStream("Concept Response.xml");
        isDocumentResponse = this.getClass().getClassLoader().getResourceAsStream("Document Response.xml");
    }

    @Test
    public void testEDMConceptRequestExport() throws JAXBException,  XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.extractEDMRequest(isConceptRequestLP);

        String XMLRequest = EDMCreators.createAsString(conceptRequestLP);
        assertNotNull(XMLRequest);
    }

    @Test
    public void testEDMDocumentRequestExport() throws JAXBException,  XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.extractEDMRequest(isDocumentRequestNP);

        String XMLRequest = EDMCreators.createAsString(conceptRequestLP);
        assertNotNull(XMLRequest);
    }

    @Test
    public void testEDMConceptResponseExport() throws JAXBException, XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.extractEDMResponse(isConceptResponse);

        String XMLResponse = EDMCreators.createAsString(conceptResponse);
        assertNotNull(XMLResponse);
    }

    @Test
    public void testEDMDocumentResponseExport() throws JAXBException, XMLStreamException {
        EDMResponse documentResponse = EDMExtractors.extractEDMResponse(isDocumentResponse);

        String XMLResponse = EDMCreators.createAsString(documentResponse);
        assertNotNull(XMLResponse);
    }

    @Test
    public void checkConsistencyConceptRequest() throws JAXBException,  XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.extractEDMRequest(isConceptRequestLP);

        String XMLRequest = EDMCreators.createAsString(conceptRequestLP);
        assertNotNull(XMLRequest);

        assertEquals(XMLRequest,
                EDMCreators.createAsString(
                        EDMExtractors.extractEDMRequest(XMLRequest)));
    }

    @Test
    public void checkConsistencyDocumentRequest() throws JAXBException,  XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.extractEDMRequest(isDocumentRequestNP);

        String XMLRequest = EDMCreators.createAsString(conceptRequestLP);
        assertNotNull(XMLRequest);

        assertEquals(XMLRequest,
                EDMCreators.createAsString(
                        EDMExtractors.extractEDMRequest(XMLRequest)));
    }


    @Test
    @Ignore
    public void checkConsistencyResponse() throws JAXBException,  XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.extractEDMResponse(isConceptResponse);

        String XMLResponse = EDMCreators.createAsString(conceptResponse);
        assertNotNull(XMLResponse);

        assertEquals(XMLResponse,
                EDMCreators.createAsString(
                        EDMExtractors.extractEDMRequest(XMLResponse)));
    }

}
