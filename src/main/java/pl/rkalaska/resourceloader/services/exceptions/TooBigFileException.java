package pl.rkalaska.resourceloader.services.exceptions;

public class TooBigFileException extends Exception {

    private static final long serialVersionUID = -1917951073913607701L;

    public TooBigFileException() {
        super();
    }
    public TooBigFileException(String msg)   {
        super(msg);
    }
    public TooBigFileException(String msg, Exception e)  {
        super(msg, e);
    }
}
