package org.woehlke.java.simpleworklist.domain.security.login;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tw on 19.02.16.
 */
@Log
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
    implements AuthenticationSuccessHandler {

    private final LoginSuccessService loginSuccessService;
    private final LocaleResolver localeResolver;

    @Autowired
    public LoginSuccessHandler(
        LoginSuccessService loginSuccessService,
        LocaleResolver localeResolver
    ) {
        super();
        this.loginSuccessService = loginSuccessService;
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
        UserAccount user = loginSuccessService.retrieveCurrentUser();
        loginSuccessService.updateLastLoginTimestamp(user);
        Locale locale;
        switch(user.getDefaultLanguage()){
            case DE: locale = Locale.GERMAN; break;
            default: locale = Locale.ENGLISH; break;
        }
        localeResolver.setLocale(request,response,locale);
        log.info("successful logged in "+user.getUserEmail());
    }

}
