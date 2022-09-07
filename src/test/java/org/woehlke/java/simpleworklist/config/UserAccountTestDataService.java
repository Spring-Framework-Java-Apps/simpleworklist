package org.woehlke.java.simpleworklist.config;

import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

public interface UserAccountTestDataService {

    void setUp();

    UserAccount getFirstUserAccount();

}
