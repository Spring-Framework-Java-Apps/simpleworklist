package org.woehlke.simpleworklist.oodm.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.user.messages.NewUser2UserMessage;
import org.woehlke.simpleworklist.oodm.entities.User2UserMessage;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

/**
 * Created by tw on 16.02.2016.
 */
public interface User2UserMessageService {

    void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, NewUser2UserMessage newUser2UserMessage);

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    Page<User2UserMessage> readAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender, Pageable request);

}
