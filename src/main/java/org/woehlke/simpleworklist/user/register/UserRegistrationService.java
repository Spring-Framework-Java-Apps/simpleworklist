package org.woehlke.simpleworklist.user.register;

public interface UserRegistrationService {

    UserRegistration findByToken(String confirmId);

    boolean registrationIsRetryAndMaximumNumberOfRetries(String email);

    void registrationCheckIfResponseIsInTime(String email);

    void registrationSendEmailTo(String email);

    void registrationSentEmail(UserRegistration o);

    void registrationClickedInEmail(UserRegistration o);

    void registrationUserCreated(UserRegistration o);

}
