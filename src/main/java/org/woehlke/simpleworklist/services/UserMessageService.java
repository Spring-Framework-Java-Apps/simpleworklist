package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;

import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
public interface UserMessageService {

    List<UserMessage> getAllMessagesBetweenCurrentAndOtherUser(long userId);

    void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, UserMessage newUserMessage);
}
