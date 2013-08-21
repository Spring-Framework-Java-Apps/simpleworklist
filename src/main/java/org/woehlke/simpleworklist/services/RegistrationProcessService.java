package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

    boolean isRetryAndMaximumNumberOfRetries(String email);

    void checkIfResponseIsInTime(String email);

    RegistrationProcess findByToken(String confirmId);


    void registerNewUserSendEmailTo(String email);

    void registerNewUserSentEmail(RegistrationProcess o);

    void registerNewUserClickedInEmail(RegistrationProcess o);

    void registerNewUserCreated(RegistrationProcess o);


    void usersPasswordChangeSendEmailTo(String email);

    void usersPasswordChangeSentEmail(RegistrationProcess o);

    void usersPasswordChangeClickedInEmail(RegistrationProcess o);

    void usersPasswordChanged(RegistrationProcess o);

}
