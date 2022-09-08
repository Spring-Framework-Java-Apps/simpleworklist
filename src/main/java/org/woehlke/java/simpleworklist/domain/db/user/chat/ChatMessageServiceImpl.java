package org.woehlke.java.simpleworklist.domain.db.user.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountChatMessage;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tw on 16.02.2016.
 */
@Slf4j
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository userMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public int getNumberOfNewIncomingMessagesForUser(
    UserAccount receiver
  ) {
    log.info("getNumberOfNewIncomingMessagesForUser");
    boolean readByReceiver = false;
    //TODO: #246 change List<Project> to Page<Project>
    List<UserAccountChatMessage> userAccountChatMessageList =
      userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
    return userAccountChatMessageList.size();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public Page<UserAccountChatMessage> findAllMessagesBetweenCurrentAndOtherUser(
    UserAccount sender, UserAccount receiver, Pageable request
  ) {
    return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender, receiver, request);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public void saveAll(List<UserAccountChatMessage> user2UserMessageList) {
    userMessageRepository.saveAll(user2UserMessageList);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public UserAccountChatMessage saveAndFlush(UserAccountChatMessage m) {
    return userMessageRepository.saveAndFlush(m);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public List<UserAccountChatMessage> findByReceiverAndReadByReceiver(UserAccount receiver, boolean readByReceiver) {
    return userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
  }

}
