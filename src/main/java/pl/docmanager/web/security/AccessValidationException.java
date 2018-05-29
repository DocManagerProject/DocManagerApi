package pl.docmanager.web.security;

public class AccessValidationException extends RuntimeException {
    public AccessValidationException(String message) {
        super(message);
    }
}
