package org.woehlke.java.simpleworklist.domain.db.user.account;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

public interface UserAccountService {

    boolean isEmailAvailable(String email);

    void createUser(UserAccountForm userAccount);

    UserAccount saveAndFlush(UserAccount u);

    UserAccount findByUserEmail(String userEmail);

    Page<UserAccount> findAll(Pageable request);

    void changeUsersPassword(UserAccountForm userAccount);

    UserAccount findById(long userId);

    Map<Long,Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver);

}
