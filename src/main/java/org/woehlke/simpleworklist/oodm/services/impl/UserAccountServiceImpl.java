package org.woehlke.simpleworklist.oodm.services.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.model.beans.UserAccountForm;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.entities.User2UserMessage;
import org.woehlke.simpleworklist.oodm.enumerations.Language;
import org.woehlke.simpleworklist.oodm.repository.ContextRepository;
import org.woehlke.simpleworklist.oodm.repository.UserAccountRepository;
import org.woehlke.simpleworklist.oodm.repository.User2UserMessageRepository;
import org.woehlke.simpleworklist.oodm.services.UserAccountService;

@Service("userAccountService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    private final User2UserMessageRepository userMessageRepository;

    private final ContextRepository contextRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, User2UserMessageRepository userMessageRepository, ContextRepository contextRepository) {
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
    public void createUser(UserAccountForm userAccount) {
        UserAccount u = new UserAccount();
        u.setUserEmail(userAccount.getUserEmail());
        u.setUserFullname(userAccount.getUserFullname());
        u.setUserPassword(encoder.encode(userAccount.getUserPassword()));
        u.setDefaultLanguage(Language.EN);
        Date now = new Date();
        u.setLastLoginTimestamp(now);
        u.setAccountNonExpired(true);
        u.setAccountNonLocked(true);
        u.setCredentialsNonExpired(true);
        u.setEnabled(true);
        LOGGER.info("About to save " + u.toString());
        u = userAccountRepository.save(u);
        Context workContext = new Context("Arbeit","Work");
        Context privContext = new Context("Privat","Private");
        workContext.setUserAccount(u);
        privContext.setUserAccount(u);
        LOGGER.info("About to save " + workContext.toString());
        contextRepository.save(workContext);
        LOGGER.info("About to save " + privContext.toString());
        contextRepository.save(privContext);
        u.setDefaultContext(workContext);
        u = userAccountRepository.save(u);
        LOGGER.info("Saved " + u.toString());
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
    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
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
    public UserAccount findUserById(long userId) {
        return userAccountRepository.getOne(userId);
    }

    @Override
    public Map<Long, Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver) {
        Map<Long, Integer> newIncomingMessagesForEachOtherUser = new HashMap<>();
        List<UserAccount> allUsers = userAccountRepository.findAll();
        for(UserAccount sender :allUsers ){
            if(receiver.getId().longValue() == sender.getId().longValue()){
                newIncomingMessagesForEachOtherUser.put(sender.getId(),0);
            } else {
                List<User2UserMessage> user2UserMessages = userMessageRepository.findBySenderAndReceiverAndReadByReceiver(sender,receiver,false);
                newIncomingMessagesForEachOtherUser.put(sender.getId(), user2UserMessages.size());
            }
        }
        return newIncomingMessagesForEachOtherUser;
    }

}