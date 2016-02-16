package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.UserMessage;

import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
public interface UserMessageService {

    List<UserMessage> getAllMessagesBetweenCurrentAndOtherUser(long userId);
}
