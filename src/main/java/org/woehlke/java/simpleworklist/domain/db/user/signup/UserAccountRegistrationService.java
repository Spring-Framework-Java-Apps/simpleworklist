package org.woehlke.java.simpleworklist.domain.db.user.signup;

import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;

public interface UserAccountRegistrationService {

    UserAccountRegistration findByToken(String confirmId);

    boolean registrationIsRetryAndMaximumNumberOfRetries(String email);

    void registrationCheckIfResponseIsInTime(String email);

    void registrationSendEmailTo(String email);

    void registrationSentEmail(UserAccountRegistration o);

    void registrationClickedInEmail(UserAccountRegistration o);

    void registrationUserCreated(UserAccountRegistration o);

}
