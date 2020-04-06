package org.woehlke.simpleworklist.config;

import org.woehlke.simpleworklist.user.account.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
