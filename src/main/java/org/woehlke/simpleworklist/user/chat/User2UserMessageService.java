package org.woehlke.simpleworklist.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.user.account.UserAccount;

/**
 * Created by tw on 16.02.2016.
 */
public interface User2UserMessageService {

    void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, User2UserMessageFormBean user2UserMessageFormBean);

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    Page<User2UserMessage> readAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender, Pageable request);

}