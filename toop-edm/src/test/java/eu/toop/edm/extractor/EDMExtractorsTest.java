package eu.toop.edm.extractor;

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
    public void testEDMRequestImport() throws JAXBException, EDMException, XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.importEDMRequest(isConceptRequestLP);
        EDMRequest conceptRequestNP = EDMExtractors.importEDMRequest(isConceptRequestNP);
        EDMRequest documentRequestLP = EDMExtractors.importEDMRequest(isDocumentRequestLP);
        EDMRequest documentRequestNP = EDMExtractors.importEDMRequest(isDocumentRequestNP);

        assertNotNull(conceptRequestLP);
        assertNotNull(conceptRequestNP);
        assertNotNull(documentRequestLP);
        assertNotNull(documentRequestNP);
    }

    @Test
    public void testEDMResponseImport() throws JAXBException, EDMException, XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.importEDMResponse(isConceptResponse);
        EDMResponse documentResponse = EDMExtractors.importEDMResponse(isDocumentResponse);

        assertNotNull(conceptResponse);
        assertNotNull(documentResponse);
    }

    @Test
    public void testEDMRequestExport() throws JAXBException, EDMException, XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.importEDMRequest(isConceptRequestLP);

        String XMLRequest = EDMExtractors.exportEDMRequestAsString(conceptRequestLP);
        assertNotNull(XMLRequest);
    }

    @Test
    public void testEDMResponseExport() throws JAXBException, EDMException, XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.importEDMResponse(isConceptResponse);

        String XMLResponse = EDMExtractors.exportEDMResponseAsString(conceptResponse);
        assertNotNull(XMLResponse);
    }

    @Test(expected = EDMException.class)
    public void testRequestImportWithResponseImport() throws JAXBException, EDMException, XMLStreamException {
        EDMExtractors.importEDMResponse(isConceptRequestLP);
        fail();
    }

    @Test(expected = EDMException.class)
    public void testResponseImportWithRequestImport() throws JAXBException, EDMException, XMLStreamException {
        EDMExtractors.importEDMRequest(isConceptResponse);
        fail();
    }

    @Test
    @Ignore
    public void checkConsistencyRequest() throws JAXBException, EDMException, XMLStreamException {
        EDMRequest conceptRequestLP = EDMExtractors.importEDMRequest(isConceptRequestLP);

        String XMLRequest = EDMExtractors.exportEDMRequestAsString(conceptRequestLP);
        assertNotNull(XMLRequest);

        assertEquals(XMLRequest,
                EDMExtractors.exportEDMRequestAsString(
                        EDMExtractors.importEDMRequest(XMLRequest)));

    }

    @Test
    @Ignore
    public void checkConsistencyResponse() throws JAXBException, EDMException, XMLStreamException {
        EDMResponse conceptResponse = EDMExtractors.importEDMResponse(isConceptResponse);

        String XMLResponse = EDMExtractors.exportEDMResponseAsString(conceptResponse);
        assertNotNull(XMLResponse);

        assertEquals(XMLResponse,
                EDMExtractors.exportEDMResponseAsString(
                        EDMExtractors.importEDMResponse(XMLResponse)));
    }
}