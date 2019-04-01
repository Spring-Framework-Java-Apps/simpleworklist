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
import org.woehlke.simpleworklist.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.UserPasswordRecoveryService;
import org.woehlke.simpleworklist.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

@Controller
public class UserPasswordRecoveryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPasswordRecoveryController.class);

    private final UserAccountService userAccountService;

    private final UserPasswordRecoveryService userPasswordRecoveryService;

    @Autowired
    public UserPasswordRecoveryController(UserAccountService userAccountService, UserPasswordRecoveryService userPasswordRecoveryService) {
        this.userAccountService = userAccountService;
        this.userPasswordRecoveryService = userPasswordRecoveryService;
    }

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
        return "user/resetPasswordForm";
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
            return "user/resetPasswordForm";
        } else {
            LOGGER.info(registerFormBean.toString());
            LOGGER.info(result.toString());
            LOGGER.info(model.toString());
            if (userAccountService.findByUserEmail(registerFormBean.getEmail()) == null) {
                String objectName = "registerFormBean";
                String field = "email";
                String defaultMessage = "This Email is not registered.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/resetPasswordForm";
            } else {
                userPasswordRecoveryService.passwordRecoverySendEmailTo(registerFormBean.getEmail());
                return "user/resetPasswordSentMail";
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
        UserPasswordRecovery o = userPasswordRecoveryService.findByToken(confirmId);
        if (o != null) {
            userPasswordRecoveryService.passwordRecoveryClickedInEmail(o);
            UserAccount ua = userAccountService.findByUserEmail(o.getEmail());
            UserAccountFormBean userAccountFormBean = new UserAccountFormBean();
            userAccountFormBean.setUserEmail(o.getEmail());
            userAccountFormBean.setUserFullname(ua.getUserFullname());
            model.addAttribute("userAccountFormBean", userAccountFormBean);
            return "user/resetPasswordConfirmed";
        } else {
            return "user/resetPasswordNotConfirmed";
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
        UserPasswordRecovery o = userPasswordRecoveryService.findByToken(confirmId);
        boolean passwordsMatch = userAccountFormBean.passwordsAreTheSame();
        if (o != null) {
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.changeUsersPassword(userAccountFormBean);
                userPasswordRecoveryService.passwordRecoveryDone(o);
                return "user/resetPasswordDone";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountFormBean";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/resetPasswordConfirmed";
            }
        } else {
            return "user/resetPasswordNotConfirmed";
        }
    }
}
