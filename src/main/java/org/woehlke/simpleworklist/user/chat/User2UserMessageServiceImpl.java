package org.woehlke.simpleworklist.user.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Created by tw on 16.02.2016.
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class User2UserMessageServiceImpl implements User2UserMessageService {

    private final User2UserMessageRepository userMessageRepository;

    @Autowired
    public User2UserMessageServiceImpl(User2UserMessageRepository userMessageRepository) {
        this.userMessageRepository = userMessageRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void sendNewUserMessage(
        UserAccount thisUser,
        UserAccount otherUser,
        User2UserMessageFormBean user2UserMessageFormBean
    ) {
        log.info("sendNewUserMessage");
        User2UserMessage m = new User2UserMessage();
        m.setSender(thisUser);
        m.setReceiver(otherUser);
        m.setReadByReceiver(false);
        m.setMessageText(user2UserMessageFormBean.getMessageText());
        userMessageRepository.saveAndFlush(m);
    }

    @Override
    public int getNumberOfNewIncomingMessagesForUser(
        UserAccount receiver
    ) {
        log.info("getNumberOfNewIncomingMessagesForUser");
        boolean readByReceiver = false;
        List<User2UserMessage> user2UserMessageList =
                userMessageRepository.findByReceiverAndReadByReceiver(receiver, readByReceiver);
        return user2UserMessageList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Page<User2UserMessage> readAllMessagesBetweenCurrentAndOtherUser(
        UserAccount receiver,
        UserAccount sender,
        Pageable request
    ) {
        log.info("readAllMessagesBetweenCurrentAndOtherUser");
        Page<User2UserMessage> user2UserMessagePage = userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
        for(User2UserMessage user2UserMessage : user2UserMessagePage){
            if((!user2UserMessage.getReadByReceiver()) && (receiver.getId().longValue()== user2UserMessage.getReceiver().getId().longValue())){
                user2UserMessage.setReadByReceiver(true);
                userMessageRepository.saveAndFlush(user2UserMessage);
            }
        }
        return userMessageRepository.findAllMessagesBetweenCurrentAndOtherUser(sender,receiver,request);
    }
}
