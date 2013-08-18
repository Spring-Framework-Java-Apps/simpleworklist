package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

    boolean isRetryAndMaximumNumberOfRetries(String email);

    void checkIfResponseIsInTime(String email);

    RegistrationProcess findByToken(String confirmId);


    void sendEmailForVerification(String email);

    void sentEmailToRegisterNewUser(RegistrationProcess o);

    void registratorClickedInEmail(RegistrationProcess o);

    void userCreated(RegistrationProcess o);


    void usersPasswordChangeSendEmailTo(String email);

    void usersPasswordChangeSentEmail(RegistrationProcess o);

    void usersPasswordChangeClickedInEmail(RegistrationProcess o);

    void usersPasswordChanged(RegistrationProcess o);

}
