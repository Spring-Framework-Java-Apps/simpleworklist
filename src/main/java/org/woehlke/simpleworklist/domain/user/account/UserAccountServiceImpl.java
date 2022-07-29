package org.woehlke.simpleworklist.domain.user.account;


import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.chat.ChatMessage;
import org.woehlke.simpleworklist.domain.context.ContextRepository;
import org.woehlke.simpleworklist.domain.chat.ChatMessageRepository;

@Slf4j
@Service("userAccountService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final ChatMessageRepository userMessageRepository;
    private final ContextRepository contextRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, ChatMessageRepository userMessageRepository, ContextRepository contextRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userMessageRepository = userMessageRepository;
        this.contextRepository = contextRepository;
        int strength = 10;
        this.encoder = new BCryptPasswordEncoder(strength);
    }

    public boolean isEmailAvailable(String email) {
        return userAccountRepository.findByUserEmail(email) == null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createUser(UserAccountForm userAccountForm) {
        String userEmail = userAccountForm.getUserEmail();
        String userFullname = userAccountForm.getUserFullname();
        String userPassword = encoder.encode(userAccountForm.getUserPassword());
        Context workContext = new Context("Arbeit", "Work");
        Context privContext = new Context("Privat", "Private");
        Context contexts[] = {workContext, privContext};
        UserAccount u = UserAccount.createUserAccount(userEmail,userFullname,userPassword,contexts);
        u = userAccountRepository.save(u);
        workContext.setUserAccount(u);
        privContext.setUserAccount(u);
        workContext = contextRepository.save(workContext);
        privContext = contextRepository.save(privContext);
        u = userAccountRepository.save(u);
        log.info("Saved " + u.toString());
        log.info("Saved " + workContext.toString());
        log.info("Saved " + privContext.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public UserAccount saveAndFlush(UserAccount u) {
        return userAccountRepository.saveAndFlush(u);
    }

    @Override
    public UserAccount findByUserEmail(String userEmail) {
        return userAccountRepository.findByUserEmail(userEmail);
    }

    @Override
    public Page<UserAccount> findAll(Pageable request) {
        return userAccountRepository.findAll(request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void changeUsersPassword(UserAccountForm userAccount) {
        UserAccount ua = userAccountRepository.findByUserEmail(userAccount.getUserEmail());
        if(ua != null) {
            String pwEncoded = encoder.encode(userAccount.getUserPassword());
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
        }
    }

    @Override
    public UserAccount findById(long userId) {
        return userAccountRepository.getReferenceById(userId);
    }

    @Override
    public Map<Long, Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver) {
        Map<Long, Integer> newIncomingMessagesForEachOtherUser = new HashMap<>();
        List<UserAccount> allUsers = userAccountRepository.findAll();
        for(UserAccount sender :allUsers ){
            if(receiver.getId().longValue() == sender.getId().longValue()){
                newIncomingMessagesForEachOtherUser.put(sender.getId(),0);
            } else {
                List<ChatMessage> chatMessages = userMessageRepository.findBySenderAndReceiverAndReadByReceiver(sender,receiver,false);
                newIncomingMessagesForEachOtherUser.put(sender.getId(), chatMessages.size());
            }
        }
        return newIncomingMessagesForEachOtherUser;
    }

}
