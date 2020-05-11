package eu.toop.edm.extractor;

import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

public final class EDMExtractors {

    // For request
    public static EDMRequest importEDMRequest(File f) throws JAXBException, EDMException {
        if (isQueryRequest(f))
            return importEDMRequest(RegRep4Reader.queryRequest().read(f));
        throw new EDMException("Are you trying to import a Request using the Response method?");
    }

    public static EDMRequest importEDMRequest(InputStream is) throws JAXBException, EDMException {
        if (isQueryRequest(is))
            return importEDMRequest(RegRep4Reader.queryRequest().read(is));
        throw new EDMException("Are you trying to import a Response using the Request method?");
    }

    public static EDMRequest importEDMRequest(Path p) throws JAXBException, EDMException {
        if (isQueryRequest(p))
            return importEDMRequest(RegRep4Reader.queryRequest().read(p));
        throw new EDMException("Are you trying to import a Response using the Request method?");
    }

    public static EDMRequest importEDMRequest(String s) throws JAXBException {
        QueryRequest queryRequest = RegRep4Reader.queryRequest().read(s);
        return importEDMRequest(queryRequest);
    }

    public static EDMRequest importEDMRequest(QueryRequest qr) throws JAXBException {
        return EDMRequestExtractor.extract(qr);
    }

    public static String exportEDMRequestAsString(EDMRequest edmRequest) {
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsString(exportEDMRequestAsQueryRequest(edmRequest));
    }

    public static InputStream exportEDMRequestAsInputStream(EDMRequest edmRequest) {
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsInputStream(exportEDMRequestAsQueryRequest(edmRequest));
    }

    public static QueryRequest exportEDMRequestAsQueryRequest(EDMRequest edmRequest) {
        return EDMRequestExtractor.extract(edmRequest);
    }


    // For response
    public static EDMResponse importEDMResponse(File f) throws JAXBException, EDMException {
        if (!isQueryRequest(f))
            return importEDMResponse(RegRep4Reader.queryResponse().read(f));
        throw new EDMException("Are you trying to import a Request using the Response method?");
    }

    public static EDMResponse importEDMResponse(InputStream is) throws JAXBException, EDMException {
        if (!isQueryRequest(is))
            return importEDMResponse(RegRep4Reader.queryResponse().read(is));
        throw new EDMException("Are you trying to import a Request using the Response method?");
    }

    public static EDMResponse importEDMResponse(Path p) throws JAXBException, EDMException {
        if (!isQueryRequest(p))
            return importEDMResponse(RegRep4Reader.queryResponse().read(p));
        throw new EDMException("Are you trying to import a Request using the Response method?");
    }

    public static EDMResponse importEDMResponse(String s) throws JAXBException, EDMException {
        if (!isQueryRequest(s))
            return importEDMResponse(RegRep4Reader.queryResponse().read(s));
        throw new EDMException("Are you trying to import a Request using the Response method?");
    }

    public static EDMResponse importEDMResponse(QueryResponse queryResponse) throws JAXBException {
        return EDMResponseExtractor.extract(queryResponse);
    }

    public static String exportEDMResponseAsString(EDMResponse edmResponse) {
        return RegRep4Writer
                .queryResponse(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsString(exportEDMResponseAsQueryResponse(edmResponse));
    }

    public static InputStream exportEDMResponseAsInputStream(EDMResponse edmResponse) {
        return RegRep4Writer
                .queryResponse(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsInputStream(exportEDMResponseAsQueryResponse(edmResponse));
    }

    public static QueryResponse exportEDMResponseAsQueryResponse(EDMResponse queryResponse) {
        return EDMResponseExtractor.extract(queryResponse);
    }


    // Check if a supplied document is a queryRequest
    public static boolean isQueryRequest(String s) {
        return s.contains("QueryRequest");
    }

    public static boolean isQueryRequest(InputStream is) throws EDMException {
        is.mark(1024);
        try {
            byte[] contents = is.readNBytes(1024);
            is.reset();
            return isQueryRequest(new String(contents, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new EDMException("Could not determine if the document was a QueryRequest or a QueryResponse");
        }
    }

    public static boolean isQueryRequest(File f) throws EDMException {
        return isQueryRequest(f.toPath());
    }

    public static boolean isQueryRequest(Path p) throws EDMException {
        try {
            return isQueryRequest(Files
                    .lines(p)
                    .findFirst()
                    .orElseThrow());
        } catch (IOException | NoSuchElementException e) {
            throw new EDMException("Could not determine if the document was a QueryRequest or a QueryResponse");
        }
    }

    private EDMExtractors() {
        // Prevent utility class instantiation
    }
}
