package org.woehlke.simpleworklist.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;

public interface UserService extends UserDetailsService {
	
	boolean isEmailAvailable(String email);
	void createUser(UserAccountFormBean userAccount, RegistrationProcess o);
	boolean authorize(LoginFormBean loginFormBean);
	String retrieveUsername();
	UserAccount retrieveCurrentUser() throws UsernameNotFoundException;
	UserAccount saveAndFlush(UserAccount u);
	UserAccount findByUserEmail(String userEmail);
	void deleteAll();
	List<UserAccount> findAll();

    void changeUsersPassword(UserAccountFormBean userAccount, RegistrationProcess o);
}
