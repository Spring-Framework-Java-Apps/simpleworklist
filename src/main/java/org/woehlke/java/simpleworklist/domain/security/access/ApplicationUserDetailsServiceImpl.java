package org.woehlke.java.simpleworklist.domain.security.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountRepository;

@Slf4j
@Service//("userDetailsService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ApplicationUserDetailsServiceImpl implements ApplicationUserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public ApplicationUserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return new UserDetailsDto(account);
    }
}
