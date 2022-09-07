package org.woehlke.java.simpleworklist.domain.user.passwordrecovery;

public interface UserPasswordRecoveryService {

    UserAccountPasswordRecovery findByToken(String confirmId);

    boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email);

    void passwordRecoveryCheckIfResponseIsInTime(String email);

    void passwordRecoverySendEmailTo(String email);

    void passwordRecoverySentEmail(UserAccountPasswordRecovery o);

    void passwordRecoveryClickedInEmail(UserAccountPasswordRecovery o);

    void passwordRecoveryDone(UserAccountPasswordRecovery o);
}
