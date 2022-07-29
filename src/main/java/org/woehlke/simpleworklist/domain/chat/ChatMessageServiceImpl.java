package org.woehlke.simpleworklist.domain.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;

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
    public ChatMessage sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        ChatMessageForm chatMessageForm
    ) {
        log.info("sendNewUserMessage");
        ChatMessage m = new ChatMessage();
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
        List<ChatMessage> chatMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
        return chatMessageList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Page<ChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver,
        UserAccount sender,
        Pageable request
    ) {
        log.info("readAllMessagesBetweenCurrentAndOtherUser");
        Page<ChatMessage> user2UserMessagePage = userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
        for(ChatMessage chatMessage : user2UserMessagePage){
            if((!chatMessage.getReadByReceiver()) && (receiver.getId().longValue()== chatMessage.getReceiver().getId().longValue())){
                chatMessage.setReadByReceiver(true);
                userMessageRepository.saveAndFlush(chatMessage);
            }
        }
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
    }
}
