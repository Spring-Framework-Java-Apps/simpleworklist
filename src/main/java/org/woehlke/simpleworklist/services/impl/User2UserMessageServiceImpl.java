package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.User2UserMessage;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.User2UserMessageRepository;
import org.woehlke.simpleworklist.services.User2UserMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
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
    public void sendNewUserMessage(UserAccount thisUser,UserAccount otherUser, User2UserMessage newUser2UserMessage) {
        LOGGER.info("sendNewUserMessage");
        newUser2UserMessage.setCreatedTimestamp(new Date());
        newUser2UserMessage.setSender(thisUser);
        newUser2UserMessage.setReceiver(otherUser);
        userMessageRepository.saveAndFlush(newUser2UserMessage);
    }

    @Override
    public List<User2UserMessage> getLast20MessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser) {
        LOGGER.info("getLast20MessagesBetweenCurrentAndOtherUser");
        Pageable pageRequest = new PageRequest(0, 20);
        Page<User2UserMessage> userMessageList =
        userMessageRepository.findFirst20MessagesBetweenCurrentAndOtherUser(thisUser,otherUser,pageRequest);
        return userMessageList.getContent();
    }

    @Override
    public int getNumberOfNewIncomingMessagesForUser(UserAccount user) {
        List<User2UserMessage> user2UserMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(user, false);
        return user2UserMessageList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void update(User2UserMessage user2UserMessage) {
        userMessageRepository.saveAndFlush(user2UserMessage);
    }

    @Override
    public List<User2UserMessage> getAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender) {
        LOGGER.info("getAllMessagesBetweenCurrentAndOtherUser");
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver);
    }
}
