package exception;
/**
 * Created by jacosro on 14/11/16.
 */
public class PeriodNotSetException extends RuntimeException {

    public PeriodNotSetException() {
        super();
    }

    public PeriodNotSetException(String message) {
        super(message);
    }
}
