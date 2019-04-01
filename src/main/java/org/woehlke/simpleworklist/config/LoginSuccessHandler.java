package org.woehlke.simpleworklist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.UserAccountAccessService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tw on 19.02.16.
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSuccessHandler.class);

    private final UserAccountAccessService userAccountAccessService;

    private final LocaleResolver localeResolver;

    @Autowired
    public LoginSuccessHandler(UserAccountAccessService userAccountAccessService, LocaleResolver localeResolver) {
        super();
        this.userAccountAccessService = userAccountAccessService;
        this.localeResolver = localeResolver;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        UserAccount user = userAccountAccessService.retrieveCurrentUser();
        userAccountAccessService.updateLastLoginTimestamp(user);
        Locale locale;
        switch(user.getDefaultLanguage()){
            case DE: locale = Locale.GERMAN; break;
            default: locale = Locale.ENGLISH; break;
        }
        localeResolver.setLocale(request,response,locale);
        LOGGER.info("successful logged in "+user.getUserEmail());
    }

}
