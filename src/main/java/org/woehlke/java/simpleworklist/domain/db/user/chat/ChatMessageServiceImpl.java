package org.woehlke.java.simpleworklist.domain.db.user.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserChatMessage;
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
    public UserChatMessage sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        ChatMessageForm chatMessageForm
    ) {
        log.info("sendNewUserMessage");
        UserChatMessage m = new UserChatMessage();
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
        List<UserChatMessage> userChatMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
        return userChatMessageList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Page<UserChatMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver,
        UserAccount sender,
        Pageable request
    ) {
        log.info("readAllMessagesBetweenCurrentAndOtherUser");
        Page<UserChatMessage> user2UserMessagePage = userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
        for(UserChatMessage userChatMessage : user2UserMessagePage){
            if((!userChatMessage.getReadByReceiver()) && (receiver.getId().longValue()== userChatMessage.getReceiver().getId().longValue())){
                userChatMessage.setReadByReceiver(true);
                userMessageRepository.saveAndFlush(userChatMessage);
            }
        }
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
    }
}
