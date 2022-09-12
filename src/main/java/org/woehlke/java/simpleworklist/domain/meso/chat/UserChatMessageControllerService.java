package org.woehlke.java.simpleworklist.domain.meso.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountChatMessage;


public interface UserChatMessageControllerService {

  UserAccountChatMessage sendNewUserMessage(
    UserAccount thisUser,
    UserAccount otherUser,
    ChatMessageForm chatMessageForm
  );

  Page<UserAccountChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
    UserAccount receiver,
    UserAccount sender,
    Pageable request
  );
}
