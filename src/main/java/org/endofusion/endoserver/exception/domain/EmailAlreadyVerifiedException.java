package org.endofusion.endoserver.exception.domain;

public class EmailAlreadyVerifiedException extends Exception {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
