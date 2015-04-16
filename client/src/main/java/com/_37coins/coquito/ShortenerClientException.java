package com._37coins.coquito;


public class ShortenerClientException extends Exception{
    private static final long serialVersionUID = -8827307266482255341L;
    
    private Reason reason;

    public ShortenerClientException() {
    }

    public ShortenerClientException(Reason reason) {
        this.reason = reason;
    }

    public ShortenerClientException(Reason reason, Throwable cause) {
        super(cause);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ShortenerClientException{" +
                "reason=" + reason +
                '}';
    }

    public enum Reason {
        INVALID_URI,
        ERROR_GETTING_RESOURCE,
        ERROR_PARSING
    }

}
