package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface TestHelperService {

    void deleteAllRegistrationProcess();

    void deleteAllActionItem();

    void deleteAllCategory();

    void deleteUserAccount();

    int getNumberOfAllRegistrations();

    RegistrationProcess findByEmailRegistration(String email);

    RegistrationProcess findByEmailPasswordRecovery(String email);
}
