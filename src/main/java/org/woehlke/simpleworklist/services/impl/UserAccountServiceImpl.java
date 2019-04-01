package org.woehlke.simpleworklist.services.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;
import org.woehlke.simpleworklist.entities.enumerations.Language;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.model.UserChangePasswordFormBean;
import org.woehlke.simpleworklist.model.UserDetailsBean;
import org.woehlke.simpleworklist.repository.ContextRepository;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.repository.UserMessageRepository;
import org.woehlke.simpleworklist.services.UserAccountService;

@Service("userAccountService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public boolean isEmailAvailable(String email) {
        return userAccountRepository.findByUserEmail(email) == null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createUser(UserAccountFormBean userAccount) {
        UserAccount u = new UserAccount();
        u.setUserEmail(userAccount.getUserEmail());
        u.setUserFullname(userAccount.getUserFullname());
        u.setUserPassword(encoder.encode(userAccount.getUserPassword()));
        u.setDefaultLanguage(Language.EN);
        Date now = new Date();
        u.setCreatedTimestamp(now);
        u.setLastLoginTimestamp(now);
        u.setAccountNonExpired(true);
        u.setAccountNonLocked(true);
        u.setCredentialsNonExpired(true);
        u.setEnabled(true);
        LOGGER.info("About to save " + u.toString());
        u = userAccountRepository.saveAndFlush(u);
        Context work = new Context("Arbeit","Work");
        Context priv = new Context("Privat","Private");
        work.setUserAccount(u);
        priv.setUserAccount(u);
        LOGGER.info("About to save " + work.toString());
        contextRepository.saveAndFlush(work);
        LOGGER.info("About to save " + priv.toString());
        contextRepository.saveAndFlush(priv);
        u.setDefaultContext(work);
        u = userAccountRepository.saveAndFlush(u);
        LOGGER.info("Saved " + u.toString());
    }

    @Override
    public boolean authorize(LoginFormBean loginFormBean) {
        return userAccountRepository.findByUserEmailAndUserPassword(loginFormBean.getUserEmail(), loginFormBean.getUserPassword()) != null;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return new UserDetailsBean(account);
    }

    @Override
    public String retrieveUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return " ";
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    public UserAccount retrieveCurrentUser() throws UsernameNotFoundException {
        String username = this.retrieveUsername();
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return account;
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
    public void changeUsersPassword(UserAccountFormBean userAccount) {
        UserAccount ua = userAccountRepository.findByUserEmail(userAccount.getUserEmail());
        if(ua != null) {
            String pwEncoded = encoder.encode(userAccount.getUserPassword());
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void changeUsersPassword(UserChangePasswordFormBean userAccountFormBean, UserAccount user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserEmail(), userAccountFormBean.getOldUserPassword());
        Authentication authenticationResult = authenticationManager.authenticate(token);
        if(authenticationResult.isAuthenticated()){
            UserAccount ua = userAccountRepository.findByUserEmail(user.getUserEmail());
            String pwEncoded = encoder.encode(userAccountFormBean.getUserPassword());
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateLastLoginTimestamp(UserAccount user) {
        user.setLastLoginTimestamp(new Date());
        userAccountRepository.saveAndFlush(user);
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
                List<UserMessage> userMessages = userMessageRepository.findBySenderAndReceiverAndReadByReceiver(sender,receiver,false);
                newIncomingMessagesForEachOtherUser.put(sender.getId(),userMessages.size());
            }
        }
        return newIncomingMessagesForEachOtherUser;
    }

    @Override
    public boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, oldUserPassword);
        Authentication authenticationResult = authenticationManager.authenticate(token);
        String oldPwEncoded = encoder.encode(oldUserPassword);
        LOGGER.info(userEmail+", "+oldPwEncoded);
        return authenticationResult.isAuthenticated();
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(token);
        if (authenticationResult.isAuthenticated()) {
            UserAccount ua = userAccountRepository.findByUserEmail(user.getUsername());
            String pwEncoded = encoder.encode(newPassword);
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
            return new UserDetailsBean(ua);
        } else {
            return user;
        }
    }

}
