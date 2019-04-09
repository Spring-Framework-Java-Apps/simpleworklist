package org.woehlke.simpleworklist.oodm.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.model.beans.NewUser2UserMessage;
import org.woehlke.simpleworklist.oodm.entities.User2UserMessage;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.repository.User2UserMessageRepository;
import org.woehlke.simpleworklist.oodm.services.User2UserMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class User2UserMessageServiceImpl implements User2UserMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(User2UserMessageServiceImpl.class);

    private final User2UserMessageRepository userMessageRepository;

    @Autowired
    public User2UserMessageServiceImpl(User2UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void sendNewUserMessage(UserAccount thisUser, UserAccount otherUser, NewUser2UserMessage newUser2UserMessage) {
        LOGGER.info("sendNewUserMessage");
        User2UserMessage m = new User2UserMessage();
        m.setSender(thisUser);
        m.setReceiver(otherUser);
        m.setReadByReceiver(false);
        m.setMessageText(newUser2UserMessage.getMessageText());
        userMessageRepository.saveAndFlush(m);
    }

    @Override
    public int getNumberOfNewIncomingMessagesForUser(UserAccount receiver) {
        boolean readByReceiver = false;
        List<User2UserMessage> user2UserMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
        return user2UserMessageList.size();
    }

    @Override
    public Page<User2UserMessage> readAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender, Pageable request) {
        Page<User2UserMessage> user2UserMessagePage = userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
        for(User2UserMessage user2UserMessage : user2UserMessagePage){
            if((!user2UserMessage.isReadByReceiver()) && (receiver.getId().longValue()== user2UserMessage.getReceiver().getId().longValue())){
                user2UserMessage.setReadByReceiver(true);
                userMessageRepository.saveAndFlush(user2UserMessage);
            }
        }
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
    }
}
