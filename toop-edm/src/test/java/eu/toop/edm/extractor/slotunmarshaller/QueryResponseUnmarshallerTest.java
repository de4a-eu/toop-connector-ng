package eu.toop.edm.extractor.slotunmarshaller;

import eu.toop.edm.extractor.EDMExtractors;
import eu.toop.regrep.query.QueryResponse;
import junit.framework.TestCase;

import javax.xml.bind.JAXBException;
import java.io.InputStream;

public class QueryResponseUnmarshallerTest extends TestCase {
    InputStream isConceptRequestLP, isConceptRequestNP, isDocumentRequestNP, isDocumentRequestLP, isConceptResponse, isDocumentResponse;

    public void setUp() throws Exception {
        super.setUp();
        isConceptRequestLP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_LP.xml");
        isConceptRequestNP = this.getClass().getClassLoader().getResourceAsStream("Concept Request_NP.xml");
        isDocumentRequestLP = this.getClass().getClassLoader().getResourceAsStream("Document Request_LP.xml");
        isDocumentRequestNP = this.getClass().getClassLoader().getResourceAsStream("Document Request_NP.xml");
        isConceptResponse = this.getClass().getClassLoader().getResourceAsStream("Concept Response.xml");
        isDocumentResponse = this.getClass().getClassLoader().getResourceAsStream("Document Response.xml");
    }

    public void testUnmarshalFile() throws JAXBException {
        QueryResponse qr = QueryResponseUnmarshaller.unmarshal(isDocumentResponse);
        var x = EDMExtractors.importEDMResponse(qr);
        System.out.println(x);
    }
    public void testUnmarshalIS() {
    }
}