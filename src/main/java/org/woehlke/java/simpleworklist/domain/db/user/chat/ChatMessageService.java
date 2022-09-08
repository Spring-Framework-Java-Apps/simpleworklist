package org.woehlke.java.simpleworklist.domain.db.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountChatMessage;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
public interface ChatMessageService {

  int getNumberOfNewIncomingMessagesForUser(UserAccount user);

  Page<UserAccountChatMessage> findAllMessagesBetweenCurrentAndOtherUser(
    UserAccount sender,
    UserAccount receiver,
    Pageable request
  );

  void saveAll(List<UserAccountChatMessage> user2UserMessageList);

  UserAccountChatMessage saveAndFlush(UserAccountChatMessage m);

  List<UserAccountChatMessage> findByReceiverAndReadByReceiver(UserAccount receiver, boolean readByReceiver);
}
