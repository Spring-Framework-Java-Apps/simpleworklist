package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.UserRegistration;

public interface UserRegistrationService {

    UserRegistration findByToken(String confirmId);

    boolean registrationIsRetryAndMaximumNumberOfRetries(String email);

    void registrationCheckIfResponseIsInTime(String email);

    void registrationSendEmailTo(String email);

    void registrationSentEmail(UserRegistration o);

    void registrationClickedInEmail(UserRegistration o);

    void registrationUserCreated(UserRegistration o);

/*
    boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email);

    void passwordRecoveryCheckIfResponseIsInTime(String email);

    void passwordRecoverySendEmailTo(String email);

    void passwordRecoverySentEmail(UserRegistration o);

    void passwordRecoveryClickedInEmail(UserRegistration o);

    void passwordRecoveryDone(UserRegistration o);
*/
}
