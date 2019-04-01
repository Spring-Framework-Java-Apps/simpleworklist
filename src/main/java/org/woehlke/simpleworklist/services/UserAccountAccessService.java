package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserChangePasswordFormBean;

public interface UserAccountAccessService {

    void changeUsersPassword(UserChangePasswordFormBean userAccountFormBean, UserAccount user);

    boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword);

    boolean authorize(LoginFormBean loginFormBean);
}
