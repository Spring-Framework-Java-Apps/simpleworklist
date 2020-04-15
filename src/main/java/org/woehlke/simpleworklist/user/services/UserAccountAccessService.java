package org.woehlke.simpleworklist.user.services;

import org.woehlke.simpleworklist.user.account.UserChangePasswordForm;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.user.login.LoginForm;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
