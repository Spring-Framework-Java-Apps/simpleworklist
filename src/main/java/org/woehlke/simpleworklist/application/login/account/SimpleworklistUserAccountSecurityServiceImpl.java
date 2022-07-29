package org.woehlke.simpleworklist.application.login.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.application.login.account.SimpleworklistUserAccountSecurityService;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.domain.user.account.UserAccountRepository;
import org.woehlke.simpleworklist.domain.user.account.UserDetailsBean;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SimpleworklistUserAccountSecurityServiceImpl implements SimpleworklistUserAccountSecurityService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public SimpleworklistUserAccountSecurityServiceImpl(UserAccountRepository userAccountRepository) {
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
