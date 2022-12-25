package org.woehlke.java.simpleworklist.domain.db.user.accountpassword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountRepository;
import org.woehlke.java.simpleworklist.domain.security.access.UserDetailsDto;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountPasswordServiceImpl implements UserAccountPasswordService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;
    //private final AuthenticationManager authenticationManager;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public UserAccountPasswordServiceImpl(
        UserAccountRepository userAccountRepository,
        //AuthenticationManager authenticationManager,
        AuthenticationProvider authenticationProvider
    ) {
        this.userAccountRepository = userAccountRepository;
        this.authenticationProvider = authenticationProvider;
        int strength = 10;
        this.encoder = new BCryptPasswordEncoder(strength);
        //this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticationResult = authenticationProvider.authenticate(token);
        if (authenticationResult.isAuthenticated()) {
            UserAccount ua = userAccountRepository.findByUserEmail(user.getUsername());
            String pwEncoded = encoder.encode(newPassword);
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
            return new UserDetailsDto(ua);
        } else {
            return user;
        }
    }
}
