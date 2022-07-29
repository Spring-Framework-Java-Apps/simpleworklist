package org.woehlke.simpleworklist.domain.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.domain.user.chat.ChatMessage;
import org.woehlke.simpleworklist.domain.user.chat.ChatMessageForm;

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
