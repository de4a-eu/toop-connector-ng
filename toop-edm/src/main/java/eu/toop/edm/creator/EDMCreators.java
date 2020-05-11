package eu.toop.edm.creator;

import eu.toop.edm.model.EDMRequest;
import eu.toop.edm.model.EDMResponse;
import eu.toop.edm.xml.cagv.CCAGV;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;

import java.io.InputStream;

public class EDMCreators {

    public static String createAsString(EDMRequest edmRequest) {
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsString(createAsQueryRequest(edmRequest));
    }

    public static InputStream createAsInputStream(EDMRequest edmRequest) {
        return RegRep4Writer
                .queryRequest(CCAGV.XSDS)
                .setFormattedOutput(true)
                .getAsInputStream(createAsQueryRequest(edmRequest));
    }

    public static QueryRequest createAsQueryRequest(EDMRequest edmRequest) {
        return edmRequest.getAsQueryRequest();
    }


    public static String createAsString(EDMResponse edmResponse) {
        return RegRep4Writer
                .queryResponse(CCCEV.XSDS)
                .setFormattedOutput(true)
                .getAsString(createAsQueryResponse(edmResponse));
    }

    public static InputStream createAsInputStream(EDMResponse edmResponse) {
        return RegRep4Writer
                .queryResponse(CCCEV.XSDS)
                .setFormattedOutput(true)
                .getAsInputStream(createAsQueryResponse(edmResponse));
    }

    public static QueryResponse createAsQueryResponse(EDMResponse edmResponse) {
        return edmResponse.getAsQueryResponse();
    }
}
