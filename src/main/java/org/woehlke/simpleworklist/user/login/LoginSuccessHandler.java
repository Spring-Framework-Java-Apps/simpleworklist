package org.woehlke.simpleworklist.user.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.woehlke.simpleworklist.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.services.UserAccountLoginSuccessService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tw on 19.02.16.
 */
@Log
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
    implements AuthenticationSuccessHandler {

    private final UserAccountLoginSuccessService userAccountLoginSuccessService;
    private final LocaleResolver localeResolver;

    @Autowired
    public LoginSuccessHandler(
        UserAccountLoginSuccessService userAccountLoginSuccessService,
        LocaleResolver localeResolver
    ) {
        super();
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws ServletException, IOException {
        log.info("onAuthenticationSuccess");
        super.onAuthenticationSuccess(request, response, authentication);
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        userAccountLoginSuccessService.updateLastLoginTimestamp(user);
        Locale locale;
        switch(user.getDefaultLanguage()){
            case DE: locale = Locale.GERMAN; break;
            default: locale = Locale.ENGLISH; break;
        }
        localeResolver.setLocale(request,response,locale);
        log.info("successful logged in "+user.getUserEmail());
    }

}
