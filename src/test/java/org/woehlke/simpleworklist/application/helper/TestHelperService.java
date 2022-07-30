package org.woehlke.simpleworklist.application.helper;

import org.woehlke.simpleworklist.domain.user.passwordrecovery.UserPasswordRecovery;
import org.woehlke.simpleworklist.domain.user.signup.UserRegistration;

public interface TestHelperService {

    void deleteAllRegistrations();

    void deleteAllPasswordRecoveries();

    void deleteAllTasks();

    void deleteAllProjects();

    void deleteUserAccount();

    int getNumberOfAllRegistrations();

    int getNumberOfAllPasswordRecoveries();

    UserRegistration findRegistrationByEmail(String email);

    UserPasswordRecovery findPasswordRecoveryByEmail(String email);
}
