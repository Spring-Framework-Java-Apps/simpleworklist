package org.woehlke.simpleworklist.application.testdata.services;

import org.woehlke.simpleworklist.user.account.UserAccount;

public interface TestDataService {

    void createTestCategoryTreeForUserAccount(UserAccount userAccount);
}
