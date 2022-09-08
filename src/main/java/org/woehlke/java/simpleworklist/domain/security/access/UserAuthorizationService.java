package org.woehlke.java.simpleworklist.domain.security.access;

import org.woehlke.java.simpleworklist.domain.security.login.LoginForm;
import org.woehlke.java.simpleworklist.domain.db.user.accountselfservice.UserChangePasswordForm;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

public interface UserAuthorizationService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
