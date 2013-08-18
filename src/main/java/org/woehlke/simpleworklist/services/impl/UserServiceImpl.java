package org.woehlke.simpleworklist.services.impl;


import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.model.UserDetailsBean;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.services.UserService;

@Service("userService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserAccountRepository userAccountRepository;

    public boolean isEmailAvailable(String email) {
        return userAccountRepository.findByUserEmail(email) == null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void createUser(UserAccountFormBean userAccount) {
        UserAccount u = new UserAccount();
        u.setUserEmail(userAccount.getUserEmail());
        u.setUserFullname(userAccount.getUserFullname());
        u.setUserPassword(userAccount.getUserPasswordEncoded());
        LOGGER.info("About to save " + u.toString());
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
        ua.setUserPassword(userAccount.getUserPasswordEncoded());
        userAccountRepository.saveAndFlush(ua);
    }


}
