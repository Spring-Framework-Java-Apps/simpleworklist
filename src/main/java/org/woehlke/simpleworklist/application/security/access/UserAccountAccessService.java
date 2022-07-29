package org.woehlke.simpleworklist.application.security.access;

import org.woehlke.simpleworklist.domain.user.accountselfservice.UserChangePasswordForm;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.application.security.login.LoginForm;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
