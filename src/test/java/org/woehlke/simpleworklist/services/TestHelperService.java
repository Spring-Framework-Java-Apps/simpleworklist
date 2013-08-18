package org.woehlke.simpleworklist.services;

public interface TestHelperService {

    void deleteAllRegistrationProcess();

    void deleteAllActionItem();

    void deleteAllCategory();

    void deleteUserAccount();

    void deleteTimelineDay();

    void deleteTimelineMonth();

    void deleteTimelineYear();

    int getNumberOfAllRegistrations();
}
