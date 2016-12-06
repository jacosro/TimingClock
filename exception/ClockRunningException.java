package exception;

/**
 * Created by jacosro on 4/12/16.
 */
public class ClockRunningException extends RuntimeException {
    public ClockRunningException() { super(); }
    public ClockRunningException(String msg) { super(msg); }
}
