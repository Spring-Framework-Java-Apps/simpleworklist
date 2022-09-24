package org.woehlke.java.simpleworklist.domain.meso.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountChatMessage;
import org.woehlke.java.simpleworklist.domain.db.user.chat.ChatMessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
public class UserChatMessageControllerServiceImpl implements UserChatMessageControllerService {

  private final ChatMessageService chatMessageService;

  @Autowired
  public UserChatMessageControllerServiceImpl(ChatMessageService chatMessageService) {
    this.chatMessageService = chatMessageService;
  }

  @Override
  public UserAccountChatMessage sendNewUserMessage(
    UserAccount thisUser,
    UserAccount otherUser,
    ChatMessageForm chatMessageForm
  ) {
    log.info("sendNewUserMessage");
    UserAccountChatMessage m = new UserAccountChatMessage();
    m.setSender(thisUser);
    m.setReceiver(otherUser);
    m.setReadByReceiver(false);
    m.setUuid(UUID.randomUUID());
    m.setMessageText(chatMessageForm.getMessageText());
    return chatMessageService.saveAndFlush(m);
  }

  @Override
  public Page<UserAccountChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
    UserAccount receiver,
    UserAccount sender,
    Pageable request
  ) {
    log.info("readAllMessagesBetweenCurrentAndOtherUser");
    log.info("-----------------------------------------------------------------------------------------------");
    List<UserAccountChatMessage> user2UserMessageList = new ArrayList<>();
    log.info("Page<UserAccountChatMessage> user2UserMessagePage");
    log.info(sender.toString());
    log.info(receiver.toString());
    log.info(request.toString());
    log.info("Page<UserAccountChatMessage> user2UserMessagePage");
    Page<UserAccountChatMessage> user2UserMessagePage = chatMessageService.findAllMessagesBetweenCurrentAndOtherUser(sender, receiver, request);
    log.info("Page<UserAccountChatMessage> user2UserMessagePage size: " + user2UserMessagePage.stream().count());
    log.info("-----------------------------------------------------------------------------------------------");
    for (UserAccountChatMessage userAccountChatMessage : user2UserMessagePage) {
      userAccountChatMessage.setReadByReceiver(true);
      user2UserMessageList.add(userAccountChatMessage);
    }
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("userMessageRepository.saveAll(user2UserMessageList)");
    chatMessageService.saveAll(user2UserMessageList);
    log.info("-----------------------------------------------------------------------------------------------");
    return chatMessageService.findAllMessagesBetweenCurrentAndOtherUser(sender, receiver, request);
  }
}
