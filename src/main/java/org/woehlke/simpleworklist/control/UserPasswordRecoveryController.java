package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

@Controller
public class UserPasswordRecoveryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordRecoveryController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationProcessService registrationProcessService;

    /**
     * Visitor who might be Registered, but not yet logged in, clicks
     * on 'password forgotten' at login formular.
     *
     * @param model
     * @return a Formular for entering the email-adress.
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public final String passwordForgottenForm(Model model) {
        RegisterFormBean registerFormBean = new RegisterFormBean();
        model.addAttribute("registerFormBean", registerFormBean);
        return "t/user/resetPasswordForm";
    }

    /**
     * If email-address exists, send email with Link for password-Reset.
     *
     * @param registerFormBean
     * @param result
     * @param model
     * @return info page if without errors or formular again displaying error messages.
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public final String passwordForgottenPost(@Valid RegisterFormBean registerFormBean,
                                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            LOGGER.info("----------------------");
            LOGGER.info(registerFormBean.toString());
            LOGGER.info(result.toString());
            LOGGER.info(model.toString());
            LOGGER.info("----------------------");
            return "t/user/resetPasswordForm";
        } else {
            LOGGER.info(registerFormBean.toString());
            LOGGER.info(result.toString());
            LOGGER.info(model.toString());
            if (userService.findByUserEmail(registerFormBean.getEmail()) == null) {
                String objectName = "registerFormBean";
                String field = "email";
                String defaultMessage = "This Email is not registered.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "t/user/resetPasswordForm";
            } else {
                registrationProcessService.passwordRecoverySendEmailTo(registerFormBean.getEmail());
                return "t/user/resetPasswordSentMail";
            }

        }
    }

    /**
     * User clicked on Link in Email for Password-Recovery.
     *
     * @param confirmId
     * @param model
     * @return a Formular for entering the new Password.
     */
    @RequestMapping(value = "/passwordResetConfirm/{confirmId}", method = RequestMethod.GET)
    public final String enterNewPasswordFormular(@PathVariable String confirmId, Model model) {
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if (o != null) {
            registrationProcessService.passwordRecoveryClickedInEmail(o);
            UserAccount ua = userService.findByUserEmail(o.getEmail());
            UserAccountFormBean userAccountFormBean = new UserAccountFormBean();
            userAccountFormBean.setUserEmail(o.getEmail());
            userAccountFormBean.setUserFullname(ua.getUserFullname());
            model.addAttribute("userAccountFormBean", userAccountFormBean);
            return "t/user/resetPasswordConfirmed";
        } else {
            return "t/user/resetPasswordNotConfirmed";
        }
    }

    /**
     * Save new Password.
     *
     * @param userAccountFormBean
     * @param result
     * @param confirmId
     * @param model
     * @return Info Page for success or back to formular with error messages.
     */
    @RequestMapping(value = "/passwordResetConfirm/{confirmId}", method = RequestMethod.POST)
    public final String enterNewPasswordPost(@Valid UserAccountFormBean userAccountFormBean,
                                             BindingResult result,
                                             @PathVariable String confirmId,
                                             Model model) {
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        boolean passwordsMatch = userAccountFormBean.passwordsAreTheSame();
        if (o != null) {
            if (!result.hasErrors() && passwordsMatch) {
                userService.changeUsersPassword(userAccountFormBean);
                registrationProcessService.passwordRecoveryDone(o);
                return "t/user/resetPasswordDone";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountFormBean";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "t/user/resetPasswordConfirmed";
            }
        } else {
            return "t/user/resetPasswordNotConfirmed";
        }
    }
}
