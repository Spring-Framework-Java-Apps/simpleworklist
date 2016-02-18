package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;

import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
public interface UserMessageService {

    void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, UserMessage newUserMessage);

    List<UserMessage> getLast20MessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser);

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    void update(UserMessage userMessage);

    List<UserMessage> getAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender);
}
