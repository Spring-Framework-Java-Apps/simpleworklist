package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.User2UserMessage;
import org.woehlke.simpleworklist.entities.UserAccount;

import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
public interface User2UserMessageService {

    void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, User2UserMessage newUser2UserMessage);

    List<User2UserMessage> getLast20MessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser);

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    void update(User2UserMessage user2UserMessage);

    List<User2UserMessage> getAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender);
}
