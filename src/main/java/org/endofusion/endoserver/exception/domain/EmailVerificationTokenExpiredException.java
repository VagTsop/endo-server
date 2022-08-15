package org.endofusion.endoserver.exception.domain;


public class EmailVerificationTokenExpiredException extends Exception {
    public EmailVerificationTokenExpiredException(String message) {
        super(message);
    }
}