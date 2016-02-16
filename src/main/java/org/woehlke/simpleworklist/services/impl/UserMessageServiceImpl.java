package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;
import org.woehlke.simpleworklist.repository.UserMessageRepository;
import org.woehlke.simpleworklist.services.UserMessageService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.ArrayList;
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
    public List<UserMessage> getAllMessagesBetweenCurrentAndOtherUser(long userId) {
        LOGGER.info("getAllMessagesBetweenCurrentAndOtherUser: "+userId);
        List<UserMessage> userMessageList = new ArrayList<>();
        return userMessageList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void sendNewUserMessage(UserAccount thisUser,UserAccount otherUser, UserMessage newUserMessage) {
        LOGGER.info("sendNewUserMessage");
        newUserMessage.setCreatedTimestamp(new Date());
        newUserMessage.setSender(thisUser);
        newUserMessage.setReceiver(otherUser);
        userMessageRepository.saveAndFlush(newUserMessage);
    }
}
