package org.woehlke.simpleworklist.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tw on 19.02.16.
 */
@Named("myLoginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Inject
    protected UserService userService;

    @Inject
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        UserAccount user = userService.retrieveCurrentUser();
        userService.updateLastLoginTimestamp(user);
        Locale locale;
        switch(user.getDefaultLanguage()){
            case DE: locale = Locale.GERMAN; break;
            default: locale = Locale.ENGLISH; break;
        }
        localeResolver.setLocale(request,response,locale);
        LOGGER.info("successful logged in "+user.getUserEmail());
    }
}
