package pl.com.organizer.exception;

public class UnconfirmedAccountException extends RuntimeException {
    public UnconfirmedAccountException(String message) {
        super(message);
    }
}
