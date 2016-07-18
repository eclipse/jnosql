package org.apache.diana.api;


public class ExecuteAsyncQueryException extends RuntimeException {


    public ExecuteAsyncQueryException() {
        super();
    }


    public ExecuteAsyncQueryException(String message) {
        super(message);
    }

    public ExecuteAsyncQueryException(String message, Throwable cause) {
        super(message, cause);
    }


    public ExecuteAsyncQueryException(Throwable cause) {
        super(cause);
    }

    protected ExecuteAsyncQueryException(String message, Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
