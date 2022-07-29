package org.woehlke.simpleworklist.domain.user.passwordrecovery;

public interface UserPasswordRecoveryService {

    UserPasswordRecovery findByToken(String confirmId);

    boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email);

    void passwordRecoveryCheckIfResponseIsInTime(String email);

    void passwordRecoverySendEmailTo(String email);

    void passwordRecoverySentEmail(UserPasswordRecovery o);

    void passwordRecoveryClickedInEmail(UserPasswordRecovery o);

    void passwordRecoveryDone(UserPasswordRecovery o);
}