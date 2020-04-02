package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery;
import org.woehlke.simpleworklist.oodm.entities.UserRegistration;

public interface TestHelperService {

    void deleteAllRegistrationProcess();

    void deleteAllActionItem();

    void deleteAllCategory();

    void deleteUserAccount();

    int getNumberOfAllRegistrations();

    UserRegistration findByEmailRegistration(String email);

    UserPasswordRecovery findByEmailPasswordRecovery(String email);
}
