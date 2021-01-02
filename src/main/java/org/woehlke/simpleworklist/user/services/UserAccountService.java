package org.woehlke.simpleworklist.user.services;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.user.domain.account.UserAccountForm;

public interface UserAccountService {

    boolean isEmailAvailable(String email);

    void createUser(UserAccountForm userAccount);

    UserAccount saveAndFlush(UserAccount u);

    UserAccount findByUserEmail(String userEmail);

    Page<UserAccount> findAll(Pageable request);

    void changeUsersPassword(UserAccountForm userAccount);

    UserAccount findUserById(long userId);

    Map<Long,Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver);

}
