package org.woehlke.simpleworklist.user.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.domain.user.chat.User2UserMessage;
import org.woehlke.simpleworklist.domain.user.chat.User2UserMessageFormBean;

/**
 * Created by tw on 16.02.2016.
 */
public interface User2UserMessageService {

    User2UserMessage sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        User2UserMessageFormBean user2UserMessageFormBean
    );

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    Page<User2UserMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver, UserAccount sender, Pageable request
    );

}
