package org.woehlke.simpleworklist.user.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.user.account.UserAccount;

public interface UserAccountLoginSuccessService {

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    void updateLastLoginTimestamp(UserAccount user);
}
