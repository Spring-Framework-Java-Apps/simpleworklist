package org.woehlke.simpleworklist.user;

import org.woehlke.simpleworklist.user.selfservice.UserChangePasswordForm;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.user.login.LoginForm;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
