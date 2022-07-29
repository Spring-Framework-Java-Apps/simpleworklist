package org.woehlke.simpleworklist.domain.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;

/**
 * Created by tw on 16.02.2016.
 */
public interface ChatMessageService {

    ChatMessage sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        ChatMessageForm chatMessageForm
    );

    int getNumberOfNewIncomingMessagesForUser(UserAccount user);

    Page<ChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver, UserAccount sender, Pageable request
    );

}
