package org.woehlke.java.simpleworklist.domain.user.login;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

public interface UserAccountLoginSuccessService {

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    void updateLastLoginTimestamp(UserAccount user);
}
