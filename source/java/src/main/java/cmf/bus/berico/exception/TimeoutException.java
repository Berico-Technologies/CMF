package cmf.bus.berico.exception;

public class TimeoutException extends RuntimeException {

    private static final long serialVersionUID = 3794279527893386651L;
    
    public TimeoutException() {
        super();
    } 
    
    public TimeoutException(String arg0) {
        super(arg0);
    }
}
