package org.woehlke.simpleworklist.control;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.services.UserAccountAccessService;

import java.util.Locale;

@Controller
public class UserLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private UserAccountAccessService userAccountAccessService;

    /**
     * Login Formular. If User is not logged in, this page will be displayed for
     * all page-URLs which need login.
     *
     * @param model
     * @return Login Screen.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public final String loginForm(Locale locale, Model model) {
        LoginFormBean loginFormBean = new LoginFormBean();
        model.addAttribute("loginFormBean", loginFormBean);
        model.addAttribute("locale",locale.getLanguage().toLowerCase());
        return "user/loginForm";
    }

    /**
     * Perform login.
     *
     * @param loginFormBean
     * @param result
     * @param model
     * @return Shows Root Project after successful login or login form with error messages.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public final String loginPerform(@Valid LoginFormBean loginFormBean,
                               BindingResult result, Model model) {
        boolean authorized = userAccountAccessService.authorize(loginFormBean);
        if (!result.hasErrors() && authorized) {
            UserAccount user = userAccountAccessService.retrieveCurrentUser();
            userAccountAccessService.updateLastLoginTimestamp(user);
            LOGGER.info("logged in");
            return "redirect:/";
        } else {
            String objectName = "loginFormBean";
            String field = "userEmail";
            String defaultMessage = "Email or Password wrong.";
            FieldError e = new FieldError(objectName, field, defaultMessage);
            result.addError(e);
            LOGGER.info("not logged in");
            return "user/loginForm";
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (SessionStatus status, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        status.setComplete();
        return "redirect:/";
    }
}
