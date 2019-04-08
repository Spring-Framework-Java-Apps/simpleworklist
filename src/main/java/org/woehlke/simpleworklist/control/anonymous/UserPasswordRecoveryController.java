package org.woehlke.simpleworklist.control.anonymous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.model.beans.UserAccountForm;
import org.woehlke.simpleworklist.oodm.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.UserRegistrationForm;
import org.woehlke.simpleworklist.oodm.services.UserPasswordRecoveryService;
import org.woehlke.simpleworklist.oodm.services.UserAccountService;

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
        UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
        model.addAttribute("userRegistrationForm", userRegistrationForm);
        return "user/resetPassword/resetPasswordForm";
    }

    /**
     * If email-address exists, send email with Link for password-Reset.
     *
     * @param userRegistrationForm
     * @param result
     * @param model
     * @return info page if without errors or formular again displaying error messages.
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public final String passwordForgottenPost(@Valid UserRegistrationForm userRegistrationForm,
                                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            LOGGER.info("----------------------");
            LOGGER.info(userRegistrationForm.toString());
            LOGGER.info(result.toString());
            LOGGER.info(model.toString());
            LOGGER.info("----------------------");
            return "user/resetPassword/resetPasswordForm";
        } else {
            LOGGER.info(userRegistrationForm.toString());
            LOGGER.info(result.toString());
            LOGGER.info(model.toString());
            if (userAccountService.findByUserEmail(userRegistrationForm.getEmail()) == null) {
                String objectName = "userRegistrationForm";
                String field = "email";
                String defaultMessage = "This Email is not registered.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/resetPassword/resetPasswordForm";
            } else {
                userPasswordRecoveryService.passwordRecoverySendEmailTo(userRegistrationForm.getEmail());
                return "user/resetPassword/resetPasswordSentMail";
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
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(o.getEmail());
            userAccountForm.setUserFullname(ua.getUserFullname());
            model.addAttribute("userAccountForm", userAccountForm);
            return "user/resetPassword/resetPasswordConfirmed";
        } else {
            return "user/resetPassword/resetPasswordNotConfirmed";
        }
    }

    /**
     * Save new Password.
     *
     * @param userAccountForm
     * @param result
     * @param confirmId
     * @param model
     * @return Info Page for success or back to formular with error messages.
     */
    @RequestMapping(value = "/passwordResetConfirm/{confirmId}", method = RequestMethod.POST)
    public final String enterNewPasswordPost(@Valid UserAccountForm userAccountForm,
                                             BindingResult result,
                                             @PathVariable String confirmId,
                                             Model model) {
        UserPasswordRecovery o = userPasswordRecoveryService.findByToken(confirmId);
        boolean passwordsMatch = userAccountForm.passwordsAreTheSame();
        if (o != null) {
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.changeUsersPassword(userAccountForm);
                userPasswordRecoveryService.passwordRecoveryDone(o);
                return "user/resetPassword/resetPasswordDone";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountForm";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/resetPassword/resetPasswordConfirmed";
            }
        } else {
            return "user/resetPassword/resetPasswordNotConfirmed";
        }
    }
}
