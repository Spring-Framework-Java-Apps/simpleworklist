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

import java.util.List;
import java.util.UUID;

/**
 * Created by tw on 16.02.2016.
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository userMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
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
        return userMessageRepository.saveAndFlush(m);
    }

    @Override
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
    public Page<UserAccountChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver,
        UserAccount sender,
        Pageable request
    ) {
        log.info("readAllMessagesBetweenCurrentAndOtherUser");
        Page<UserAccountChatMessage> user2UserMessagePage = userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
        for(UserAccountChatMessage userAccountChatMessage : user2UserMessagePage){
            if((!userAccountChatMessage.getReadByReceiver()) && (receiver.getId().longValue()== userAccountChatMessage.getReceiver().getId().longValue())){
                userAccountChatMessage.setReadByReceiver(true);
                userMessageRepository.saveAndFlush(userAccountChatMessage);
            }
        }
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
    }
}
