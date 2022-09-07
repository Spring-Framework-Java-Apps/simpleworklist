package org.woehlke.java.simpleworklist.domain.user.login;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;

public interface LoginSuccessService {

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    void updateLastLoginTimestamp(UserAccount user);
}
