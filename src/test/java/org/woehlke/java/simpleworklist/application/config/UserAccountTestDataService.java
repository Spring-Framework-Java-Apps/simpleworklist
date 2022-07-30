package org.woehlke.java.simpleworklist.application.config;

import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
