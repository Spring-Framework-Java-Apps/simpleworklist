package org.woehlke.simpleworklist.domain.user.access;

import org.woehlke.simpleworklist.domain.user.accountselfservice.UserChangePasswordForm;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.domain.user.login.LoginForm;

public interface UserAuthorizationService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
