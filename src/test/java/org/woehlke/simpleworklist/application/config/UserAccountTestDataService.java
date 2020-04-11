package org.woehlke.simpleworklist.application.config;

import org.woehlke.simpleworklist.user.account.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
