package org.woehlke.simpleworklist.control;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class UserLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);

    @Inject
    protected UserService userService;

    /**
     * Login Formular. If User is not logged in, this page will be displayed for
     * all page-URLs which need login.
     *
     * @param model
     * @return Login Screen.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public final String loginForm(Model model) {
        LoginFormBean loginFormBean = new LoginFormBean();
        model.addAttribute("loginFormBean", loginFormBean);
        return "user/loginForm";
    }

    /**
     * Perform login.
     *
     * @param loginFormBean
     * @param result
     * @param model
     * @return Shows Root Category after successful login or login form with error messages.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public final String loginPerform(@Valid LoginFormBean loginFormBean,
                               BindingResult result, Model model) {
        boolean authorized = userService.authorize(loginFormBean);
        if (!result.hasErrors() && authorized) {
            userService.updateLastLoginTimestamp();
            return "redirect:/";
        } else {
            String objectName = "loginFormBean";
            String field = "userEmail";
            String defaultMessage = "Email or Password wrong.";
            FieldError e = new FieldError(objectName, field, defaultMessage);
            result.addError(e);
            return "user/loginForm";
        }
    }

}
