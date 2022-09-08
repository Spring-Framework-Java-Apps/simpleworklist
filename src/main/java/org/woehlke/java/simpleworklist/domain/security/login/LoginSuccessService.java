package org.woehlke.java.simpleworklist.domain.security.login;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

public interface LoginSuccessService {

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    void updateLastLoginTimestamp(UserAccount user);
}
