package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.model.beans.UserChangePasswordForm;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.LoginForm;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordForm userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginForm loginForm);
}
