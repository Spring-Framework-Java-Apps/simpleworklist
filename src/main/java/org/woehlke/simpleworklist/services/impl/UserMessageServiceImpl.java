package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;
import org.woehlke.simpleworklist.repository.UserMessageRepository;
import org.woehlke.simpleworklist.services.UserMessageService;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserMessageServiceImpl implements UserMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageServiceImpl.class);

    @Inject
    private UserMessageRepository userMessageRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void sendNewUserMessage(UserAccount thisUser,UserAccount otherUser, UserMessage newUserMessage) {
        LOGGER.info("sendNewUserMessage");
        newUserMessage.setCreatedTimestamp(new Date());
        newUserMessage.setSender(thisUser);
        newUserMessage.setReceiver(otherUser);
        userMessageRepository.saveAndFlush(newUserMessage);
    }

    @Override
    public List<UserMessage> getLast20MessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser) {
        LOGGER.info("getLast20MessagesBetweenCurrentAndOtherUser");
        Pageable pageRequest = new PageRequest(0, 20);
        Page<UserMessage> userMessageList =
        userMessageRepository.findFirst20MessagesBetweenCurrentAndOtherUser(thisUser,otherUser,pageRequest);
        return userMessageList.getContent();
    }

    @Override
    public int getNumberOfNewIncomingMessagesForUser(UserAccount user) {
        List<UserMessage> userMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(user, false);
        return userMessageList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void update(UserMessage userMessage) {
        userMessageRepository.saveAndFlush(userMessage);
    }

    @Override
    public List<UserMessage> getAllMessagesBetweenCurrentAndOtherUser(UserAccount receiver, UserAccount sender) {
        LOGGER.info("getAllMessagesBetweenCurrentAndOtherUser");
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver);
    }
}
