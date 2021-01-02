package org.woehlke.simpleworklist.application.config;

import org.woehlke.simpleworklist.user.domain.account.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
