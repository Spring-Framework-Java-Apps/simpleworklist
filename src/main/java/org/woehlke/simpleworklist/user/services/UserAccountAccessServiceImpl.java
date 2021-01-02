package org.woehlke.simpleworklist.user.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.user.domain.account.UserAccountRepository;
import org.woehlke.simpleworklist.user.login.LoginForm;
import org.woehlke.simpleworklist.user.domain.account.UserChangePasswordForm;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountAccessServiceImpl implements UserAccountAccessService {

    private final UserAccountRepository userAccountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @Autowired
    public UserAccountAccessServiceImpl(
        UserAccountRepository userAccountRepository,
        AuthenticationManager authenticationManager
    ) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationManager = authenticationManager;
        //TODO:
        int strength = 10;
        this.encoder = new BCryptPasswordEncoder(strength);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void changeUsersPassword(
        UserChangePasswordForm userAccountFormBean,
        UserAccount user
    ) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            user.getUserEmail(),
            userAccountFormBean.getOldUserPassword()
        );
        Authentication authenticationResult = authenticationManager.authenticate(token);
        if(authenticationResult.isAuthenticated()){
            UserAccount ua = userAccountRepository.findByUserEmail(user.getUserEmail());
            String pwEncoded = this.encoder.encode(userAccountFormBean.getUserPassword());
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
        }
    }

    @Override
    public boolean confirmUserByLoginAndPassword(
        String userEmail,
        String oldUserPassword
    ) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            userEmail,
            oldUserPassword
        );
        Authentication authenticationResult = authenticationManager.authenticate(token);
        String oldPwEncoded = this.encoder.encode(oldUserPassword);
        log.debug(userEmail+", "+oldPwEncoded);
        return authenticationResult.isAuthenticated();
    }

    @Override
    public boolean authorize(LoginForm loginForm) {
        UserAccount account = userAccountRepository.findByUserEmailAndUserPassword(
            loginForm.getUserEmail(),
            loginForm.getUserPassword()
        );
        return account != null;
    }
}
