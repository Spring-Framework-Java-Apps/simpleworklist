package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserDetailsBean;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.services.UserAccountSecurityService;

@Service("userAccountSecurityService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountSecurityServiceImpl implements UserAccountSecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountSecurityServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return new UserDetailsBean(account);
    }
}
