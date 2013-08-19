package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface TestHelperService {

    void deleteAllRegistrationProcess();

    void deleteAllActionItem();

    void deleteAllCategory();

    void deleteUserAccount();

    void deleteTimelineDay();

    void deleteTimelineMonth();

    void deleteTimelineYear();

    int getNumberOfAllRegistrations();

    RegistrationProcess findByEmail(String email);
}
