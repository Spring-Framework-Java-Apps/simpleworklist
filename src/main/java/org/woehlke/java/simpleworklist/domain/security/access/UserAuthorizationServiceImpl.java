package org.woehlke.java.simpleworklist.domain.security.access;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.security.login.LoginForm;
import org.woehlke.java.simpleworklist.domain.db.user.accountselfservice.UserChangePasswordForm;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountRepository;

@Log
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAuthorizationServiceImpl implements UserAuthorizationService {

    private final UserAccountRepository userAccountRepository;
    //private final AuthenticationManager authenticationManager;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder encoder;

    @Autowired
    public UserAuthorizationServiceImpl(
        UserAccountRepository userAccountRepository,
        //AuthenticationManager authenticationManager
        AuthenticationProvider authenticationProvider) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationProvider = authenticationProvider;
        //this.authenticationManager = authenticationManager;
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
        Authentication authenticationResult = authenticationProvider.authenticate(token);
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
        Authentication authenticationResult = authenticationProvider.authenticate(token);
        String oldPwEncoded = this.encoder.encode(oldUserPassword);
        log.info(userEmail+", "+oldPwEncoded);
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
