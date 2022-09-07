package org.woehlke.java.simpleworklist.application.helper;

import org.woehlke.java.simpleworklist.domain.user.passwordrecovery.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.user.signup.UserAccountRegistration;

public interface TestHelperService {

    void deleteAllRegistrations();

    void deleteAllPasswordRecoveries();

    void deleteAllTasks();

    void deleteAllProjects();

    void deleteUserAccount();

    int getNumberOfAllRegistrations();

    int getNumberOfAllPasswordRecoveries();

    UserAccountRegistration findRegistrationByEmail(String email);

    UserAccountPasswordRecovery findPasswordRecoveryByEmail(String email);
}
