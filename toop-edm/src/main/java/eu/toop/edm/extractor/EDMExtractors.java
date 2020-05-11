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
    public static EDMRequest extractEDMRequest(File f) throws JAXBException, FileNotFoundException, XMLStreamException {
        return extractEDMRequest(new FileInputStream(f));
    }

    public static EDMRequest extractEDMRequest(Path p) throws JAXBException, FileNotFoundException, XMLStreamException {
        return extractEDMRequest(p.toFile());
    }

    public static EDMRequest extractEDMRequest(String s) throws JAXBException, XMLStreamException {
        return extractEDMRequest(new NonBlockingByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public static EDMRequest extractEDMRequest(QueryRequest qr) throws JAXBException {
        return EDMRequestExtractor.extract(qr);
    }

    public static EDMRequest extractEDMRequest(InputStream is) throws JAXBException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(is);

        // Move cursor to the start of the document
        while (!eventReader.peek().isStartElement()) {
            eventReader.nextEvent();
        }

        // Peek at first element to check if it is a QueryRequest
        if ((eventReader
                .peek()
                .asStartElement()
                .getName()
                .getLocalPart()
                .equals("QueryRequest")))
            return extractEDMRequest(RegRep4Reader
                    .queryRequest(CCCEV.XSDS)
                    .read(eventReader));

        throw new IllegalArgumentException("This document does not contain a QueryRequest.");
    }

    // For response
    public static EDMResponse extractEDMResponse(File f) throws JAXBException, FileNotFoundException, XMLStreamException {
        return (extractEDMResponse(new FileInputStream(f)));
    }

    public static EDMResponse extractEDMResponse(Path p) throws JAXBException, FileNotFoundException, XMLStreamException {
        return extractEDMResponse(p.toFile());
    }

    public static EDMResponse extractEDMResponse(String s) throws JAXBException, XMLStreamException {
        return extractEDMResponse(new NonBlockingByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }

    public static EDMResponse extractEDMResponse(InputStream is) throws JAXBException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(is);

        // Move cursor to the start of the document
        while (!eventReader.peek().isStartElement()) {
            eventReader.nextEvent();
        }

        // Peek at first element to check if it is a QueryResponse
        if ((eventReader
                .peek()
                .asStartElement()
                .getName()
                .getLocalPart()
                .equals("QueryResponse")))
            return extractEDMResponse(RegRep4Reader
                    .queryResponse(CCCEV.XSDS)
                    .read(eventReader));

        throw new IllegalArgumentException("This document does not contain a QueryResponse.");
    }

    public static EDMResponse extractEDMResponse(QueryResponse queryResponse) throws JAXBException {
        return EDMResponseExtractor.extract(queryResponse);
    }

}
