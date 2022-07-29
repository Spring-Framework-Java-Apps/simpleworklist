package org.woehlke.simpleworklist.application.security.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.woehlke.simpleworklist.application.security.access.UserAccountAccessService;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;

@Slf4j
@Controller
@RequestMapping(path = "/user")
public class UserLoginController {

    private final UserAccountLoginSuccessService userAccountLoginSuccessService;
    private final UserAccountAccessService userAccountAccessService;

    @Autowired
    public UserLoginController(
        UserAccountLoginSuccessService userAccountLoginSuccessService,
        UserAccountAccessService userAccountAccessService
    ) {
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
        this.userAccountAccessService = userAccountAccessService;
    }

    /**
     * Login Formular. If User is not logged in, this page will be displayed for
     * all page-URLs which need login.
     *
     * @param model
     * @return Login Screen.
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public final String loginGet(Model model) {
        log.info("loginForm");
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "user/login/loginForm";
    }

    /**
     * Perform login.
     *
     * @param loginForm
     * @param result
     * @param model
     * @return Shows Root Project after successful login or login form with error messages.
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public final String loginPost(
        @Valid LoginForm loginForm,
        BindingResult result,
        Model model
    ) {
        log.info("loginPerform");
        boolean authorized = userAccountAccessService.authorize(loginForm);
        if (!result.hasErrors() && authorized) {
            UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
            userAccountLoginSuccessService.updateLastLoginTimestamp(user);
            log.info("logged in");
            return "redirect:/home";
        } else {
            String objectName = "loginForm";
            String field = "userEmail";
            String defaultMessage = "Email or Password wrong.";
            FieldError fieldError = new FieldError(objectName, field, defaultMessage);
            result.addError(fieldError);
            field = "userPassword";
            fieldError = new FieldError(objectName, field, defaultMessage);
            result.addError(fieldError);
            log.info("not logged in");
            return "user/login/loginForm";
        }
    }

    @RequestMapping(path="/logout", method = RequestMethod.GET)
    public String logoutPage (
        SessionStatus status,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        log.info("logoutPages");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        status.setComplete();
        return "redirect:/home";
    }
}
