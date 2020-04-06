package org.woehlke.simpleworklist.user.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.user.UserDetailsBean;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountSecurityServiceImpl implements UserAccountSecurityService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountSecurityServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return new UserDetailsBean(account);
    }
}
