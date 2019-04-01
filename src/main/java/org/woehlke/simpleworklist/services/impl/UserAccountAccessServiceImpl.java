package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserChangePasswordFormBean;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.services.UserAccountAccessService;

@Service("userAccountAccessService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountAccessServiceImpl implements UserAccountAccessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountAccessServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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
    public boolean confirmUserByLoginAndPassword(String userEmail, String oldUserPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, oldUserPassword);
        Authentication authenticationResult = authenticationManager.authenticate(token);
        String oldPwEncoded = encoder.encode(oldUserPassword);
        LOGGER.info(userEmail+", "+oldPwEncoded);
        return authenticationResult.isAuthenticated();
    }
}
