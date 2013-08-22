package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

    RegistrationProcess findByToken(String confirmId);

    boolean registrationIsRetryAndMaximumNumberOfRetries(String email);

    void registrationCheckIfResponseIsInTime(String email);

    void registrationSendEmailTo(String email);

    void registrationSentEmail(RegistrationProcess o);

    void registrationClickedInEmail(RegistrationProcess o);

    void registrationUserCreated(RegistrationProcess o);


    boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email);

    void passwordRecoveryCheckIfResponseIsInTime(String email);

    void passwordRecoverySendEmailTo(String email);

    void passwordRecoverySentEmail(RegistrationProcess o);

    void passwordRecoveryClickedInEmail(RegistrationProcess o);

    void passwordRecoveryDone(RegistrationProcess o);

}
