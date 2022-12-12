package co.com.example.secrets.exception;

public class TechnicalException extends RuntimeException{

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(Throwable cause, String message) {
        super(message, cause);
    }

}