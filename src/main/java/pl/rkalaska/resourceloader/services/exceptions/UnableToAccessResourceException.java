package pl.rkalaska.resourceloader.services.exceptions;

public class UnableToAccessResourceException extends Exception{
    private static final long serialVersionUID = -7828981700818986110L;

    public UnableToAccessResourceException() {
        super();
    }
    public UnableToAccessResourceException(String msg)   {
        super(msg);
    }
    public UnableToAccessResourceException(String msg, Exception e)  {
        super(msg, e);
    }
}


