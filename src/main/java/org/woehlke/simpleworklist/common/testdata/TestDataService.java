package org.woehlke.simpleworklist.common.testdata;

import org.woehlke.simpleworklist.user.domain.account.UserAccount;

public interface TestDataService {

    void createTestCategoryTreeForUserAccount(UserAccount userAccount);
}
