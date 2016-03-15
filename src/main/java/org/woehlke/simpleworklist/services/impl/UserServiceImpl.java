package org.woehlke.simpleworklist.services.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
import org.springframework.util.Assert;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.model.UserDetailsBean;
import org.woehlke.simpleworklist.model.UserPasswordChangeFormBean;
import org.woehlke.simpleworklist.repository.AreaRepository;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.repository.UserMessageRepository;
import org.woehlke.simpleworklist.services.UserService;

@Service("userService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserAccountRepository userAccountRepository;

    @Inject
    private UserMessageRepository userMessageRepository;

    @Inject
    private AreaRepository areaRepository;

    @Inject
    private PasswordEncoder encoder;

    @Inject
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
        u.setDefaultLocale("en");
        Date now = new Date();
        u.setCreatedTimestamp(now);
        u.setLastLoginTimestamp(now);
        LOGGER.info("About to save " + u.toString());
        u = userAccountRepository.saveAndFlush(u);
        Area work = new Area("Arbeit","Work");
        Area priv = new Area("Privat","Private");
        work.setUserAccount(u);
        priv.setUserAccount(u);
        LOGGER.info("About to save " + work.toString());
        areaRepository.saveAndFlush(work);
        LOGGER.info("About to save " + priv.toString());
        areaRepository.saveAndFlush(priv);
        u.setDefaultArea(work);
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
    public void changeUsersPassword(UserPasswordChangeFormBean userAccountFormBean, UserAccount user) {
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
        return userAccountRepository.findOne(userId);
    }

    @Override
    public Map<Long, Integer> getNewIncomingMessagesForEachOtherUser(UserAccount receiver) {
        Map<Long, Integer> newIncomingMessagesForEachOtherUser = new HashMap<>();
        List<UserAccount> allUsers = userAccountRepository.findAll();
        for(UserAccount sender :allUsers ){
            if(receiver.getId() == sender.getId()){
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

}
