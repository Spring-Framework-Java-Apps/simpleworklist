package org.woehlke.simpleworklist.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;

public interface UserAccountService {

    boolean isEmailAvailable(String email);

    void createUser(UserAccountFormBean userAccount);

    boolean authorize(LoginFormBean loginFormBean);

    String retrieveUsername();

    UserAccount retrieveCurrentUser() throws UsernameNotFoundException;

    UserAccount saveAndFlush(UserAccount u);

    UserAccount findByUserEmail(String userEmail);

    List<UserAccount> findAll();

    void changeUsersPassword(UserAccountFormBean userAccount);

    void updateLastLoginTimestamp(UserAccount user);

    UserAccount findUserById(long userId);

    Map<Long,Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver);

}
