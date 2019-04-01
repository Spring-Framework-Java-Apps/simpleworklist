package org.woehlke.simpleworklist.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserChangePasswordFormBean;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordFormBean userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    void updateLastLoginTimestamp(UserAccount user);

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;
}
