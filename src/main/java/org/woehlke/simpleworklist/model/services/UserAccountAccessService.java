package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.model.UserChangePasswordForm;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginForm;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
