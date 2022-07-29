package org.woehlke.simpleworklist.application.helper;

import org.woehlke.simpleworklist.domain.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.domain.user.register.UserRegistration;

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
