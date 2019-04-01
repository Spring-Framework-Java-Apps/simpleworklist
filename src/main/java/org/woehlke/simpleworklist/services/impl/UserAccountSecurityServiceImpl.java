package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserDetailsBean;
import org.woehlke.simpleworklist.repository.UserAccountRepository;
import org.woehlke.simpleworklist.services.UserAccountSecurityService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountSecurityServiceImpl implements UserAccountSecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountSecurityServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return new UserDetailsBean(account);
    }
}
