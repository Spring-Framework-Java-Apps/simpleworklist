package org.woehlke.java.simpleworklist.domain.db.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserChatMessage;

/**
 * Created by tw on 16.02.2016.
 */
public interface ChatMessageService {

    UserChatMessage sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        ChatMessageForm chatMessageForm
    );

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    Page<UserChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver, UserAccount sender, Pageable request
    );

}
