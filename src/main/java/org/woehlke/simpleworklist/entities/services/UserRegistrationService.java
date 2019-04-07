package org.woehlke.simpleworklist.entities.services;

import org.woehlke.simpleworklist.entities.UserRegistration;

public interface UserRegistrationService {

    UserRegistration findByToken(String confirmId);

    boolean registrationIsRetryAndMaximumNumberOfRetries(String email);

    void registrationCheckIfResponseIsInTime(String email);

    void registrationSendEmailTo(String email);

    void registrationSentEmail(UserRegistration o);

    void registrationClickedInEmail(UserRegistration o);

    void registrationUserCreated(UserRegistration o);

}
