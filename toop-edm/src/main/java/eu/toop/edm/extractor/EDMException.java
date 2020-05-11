package eu.toop.edm.extractor;

public class EDMException extends Exception {
    public EDMException() {
    }

    public EDMException(String s) {
        super(s);
    }

    public EDMException(String message, Throwable cause) {
        super(message, cause);
    }

    public EDMException(Throwable cause) {
        super(cause);
    }

    public EDMException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
