package pl.rkalaska.resourceloader.transport.exceptions;

import java.io.Serializable;

public class RestException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public RestException() {
        super();
    }
    public RestException(String msg)   {
        super(msg);
    }
    public RestException(String msg, Exception e)  {
        super(msg, e);
    }
}
