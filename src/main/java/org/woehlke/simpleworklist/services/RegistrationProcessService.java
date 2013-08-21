package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

    RegistrationProcess findByToken(String confirmId);

    boolean registerNewUserIsRetryAndMaximumNumberOfRetries(String email);

    void registerNewUserCheckIfResponseIsInTime(String email);

    void registerNewUserSendEmailTo(String email);

    void registerNewUserSentEmail(RegistrationProcess o);

    void registerNewUserClickedInEmail(RegistrationProcess o);

    void registerNewUserCreated(RegistrationProcess o);


    boolean usersPasswordChangeIsRetryAndMaximumNumberOfRetries(String email);

    void usersPasswordChangeCheckIfResponseIsInTime(String email);

    void usersPasswordChangeSendEmailTo(String email);

    void usersPasswordChangeSentEmail(RegistrationProcess o);

    void usersPasswordChangeClickedInEmail(RegistrationProcess o);

    void usersPasswordChanged(RegistrationProcess o);

}
