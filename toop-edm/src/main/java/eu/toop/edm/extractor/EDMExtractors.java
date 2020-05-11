package eu.toop.edm.extractor;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class EDMExtractors {

    private EDMExtractors() {
        // Prevent utility class instantiation
    }

    // For request
    public static EDMRequest importEDMRequest(File f) throws JAXBException, FileNotFoundException, XMLStreamException {
        return importEDMRequest(new FileInputStream(f));
    }

    public static EDMRequest importEDMRequest(Path p) throws JAXBException, FileNotFoundException, XMLStreamException {
        return importEDMRequest(p.toFile());
    }

    public static EDMRequest importEDMRequest(String s) throws JAXBException, XMLStreamException {
        return importEDMRequest(new NonBlockingByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public static EDMRequest importEDMRequest(QueryRequest qr) throws JAXBException {
        return EDMRequestExtractor.extract(qr);
    }

    public static EDMRequest importEDMRequest(InputStream is) throws JAXBException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(is);

        // Move cursor to the start of the document
        while (!eventReader.peek().isStartElement()) {
            eventReader.nextEvent();
        }

        // Peek first element to check if it is a QueryRequest
        if ((eventReader
                .peek()
                .asStartElement()
                .getName()
                .getLocalPart()
                .equals("QueryRequest")))
            return importEDMRequest(RegRep4Reader
                    .queryRequest(CCCEV.XSDS)
                    .read(eventReader));

        throw new IllegalArgumentException("This document does not contain a QueryRequest.");
    }

    // For response
    public static EDMResponse importEDMResponse(File f) throws JAXBException, FileNotFoundException, XMLStreamException {
        return (importEDMResponse(new FileInputStream(f)));
    }

    public static EDMResponse importEDMResponse(Path p) throws JAXBException, FileNotFoundException, XMLStreamException {
        return importEDMResponse(p.toFile());
    }

    public static EDMResponse importEDMResponse(String s) throws JAXBException, XMLStreamException {
        return importEDMResponse(new NonBlockingByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public static EDMResponse importEDMResponse(InputStream is) throws JAXBException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(is);

        // Move cursor to the start of the document
        while (!eventReader.peek().isStartElement()) {
            eventReader.nextEvent();
        }

        // Peek first element to check if it is a QueryResponse
        if ((eventReader
                .peek()
                .asStartElement()
                .getName()
                .getLocalPart()
                .equals("QueryResponse")))
            return importEDMResponse(RegRep4Reader
                    .queryResponse(CCCEV.XSDS)
                    .read(eventReader));

        throw new IllegalArgumentException("This document does not contain a QueryResponse.");
    }

    public static EDMResponse importEDMResponse(QueryResponse queryResponse) throws JAXBException {
        return EDMResponseExtractor.extract(queryResponse);
    }

}
