package org.endofusion.endoserver.service.token;

import org.endofusion.endoserver.domain.token.ConfirmationToken;

import java.util.Optional;


public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> getToken(String token) ;

    int setConfirmedAt(String token) ;
}