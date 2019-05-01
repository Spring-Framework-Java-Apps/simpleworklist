package org.woehlke.simpleworklist.model.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

public interface UserAccountLoginSuccessService {

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    void updateLastLoginTimestamp(UserAccount user);
}
