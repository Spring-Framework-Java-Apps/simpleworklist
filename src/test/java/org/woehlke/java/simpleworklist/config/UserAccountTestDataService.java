package org.woehlke.java.simpleworklist.config;

import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
