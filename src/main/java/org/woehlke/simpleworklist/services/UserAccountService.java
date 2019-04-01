package org.woehlke.simpleworklist.services;

import java.util.List;
import java.util.Map;

import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;

public interface UserAccountService {

    boolean isEmailAvailable(String email);

    void createUser(UserAccountFormBean userAccount);

    UserAccount saveAndFlush(UserAccount u);

    UserAccount findByUserEmail(String userEmail);

    List<UserAccount> findAll();

    void changeUsersPassword(UserAccountFormBean userAccount);

    UserAccount findUserById(long userId);

    Map<Long,Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver);

}
