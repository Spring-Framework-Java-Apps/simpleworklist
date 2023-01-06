package org.woehlke.java.simpleworklist.domain.security.login;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Log
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class LoginSuccessServiceImpl implements LoginSuccessService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public LoginSuccessServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public String retrieveUsername() {
        log.info("retrieveUsername");
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
        log.info("retrieveCurrentUser");
        String username = this.retrieveUsername();
        UserAccount account = userAccountRepository.findByUserEmail(username);
        if (account == null) throw new UsernameNotFoundException(username);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void updateLastLoginTimestamp(UserAccount user) {
        log.info("updateLastLoginTimestamp");
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);
        user.setLastLoginTimestamp(now);
        userAccountRepository.saveAndFlush(user);
    }

}
