package org.woehlke.simpleworklist.model.services;

import java.util.List;
import java.util.Map;

import org.woehlke.simpleworklist.model.UserAccountForm;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface UserAccountService {

    boolean isEmailAvailable(String email);

    void createUser(UserAccountForm userAccount);

    UserAccount saveAndFlush(UserAccount u);

    UserAccount findByUserEmail(String userEmail);

    List<UserAccount> findAll();

    void changeUsersPassword(UserAccountForm userAccount);

    UserAccount findUserById(long userId);

    Map<Long,Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver);

}
