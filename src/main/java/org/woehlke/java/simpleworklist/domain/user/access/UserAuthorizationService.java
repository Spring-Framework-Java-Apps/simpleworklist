package org.woehlke.java.simpleworklist.domain.user.access;

import org.woehlke.java.simpleworklist.domain.user.accountselfservice.UserChangePasswordForm;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.java.simpleworklist.domain.user.login.LoginForm;

public interface UserAuthorizationService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
