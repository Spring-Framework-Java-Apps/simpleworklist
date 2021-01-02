package org.woehlke.simpleworklist.user.resetpassword;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.user.domain.account.UserAccountForm;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.user.register.UserRegistrationForm;
import org.woehlke.simpleworklist.user.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.services.UserPasswordRecoveryService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(path = "/user")
public class UserPasswordRecoveryController {

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
    @RequestMapping(path="/resetPassword", method = RequestMethod.GET)
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
    @RequestMapping(path="/resetPassword", method = RequestMethod.POST)
    public final String passwordForgottenPost(
        @Valid UserRegistrationForm userRegistrationForm,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            log.debug("----------------------");
            log.debug(userRegistrationForm.toString());
            log.debug(result.toString());
            log.debug(model.toString());
            log.debug("----------------------");
            return "user/resetPassword/resetPasswordForm";
        } else {
            log.debug(userRegistrationForm.toString());
            log.debug(result.toString());
            log.debug(model.toString());
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
    @RequestMapping(path = "/resetPassword/confirm/{confirmId}", method = RequestMethod.GET)
    public final String enterNewPasswordFormular(
        @PathVariable String confirmId,
        Model model
    ) {
        UserPasswordRecovery oUserPasswordRecovery = userPasswordRecoveryService.findByToken(confirmId);
        if (oUserPasswordRecovery != null) {
            userPasswordRecoveryService.passwordRecoveryClickedInEmail(oUserPasswordRecovery);
            UserAccount ua = userAccountService.findByUserEmail(oUserPasswordRecovery.getEmail());
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(oUserPasswordRecovery.getEmail());
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
    @RequestMapping(path =  "/resetPassword/confirm/{confirmId}", method = RequestMethod.POST)
    public final String enterNewPasswordPost(
        @Valid UserAccountForm userAccountForm,
        BindingResult result,
        @PathVariable String confirmId,
        Model model
    ) {
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
