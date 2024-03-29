package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountService;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountForm;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPasswordRecovery;
import org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery.UserAccountPasswordRecoveryService;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationForm;

import jakarta.validation.Valid;

@Log
@Controller
@RequestMapping(path= "/user/resetPassword")
public class UserPasswordRecoveryController {

    private final UserAccountPasswordRecoveryService userAccountPasswordRecoveryService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserPasswordRecoveryController(
        UserAccountPasswordRecoveryService userAccountPasswordRecoveryService,
        UserAccountService userAccountService
    ) {
        this.userAccountPasswordRecoveryService = userAccountPasswordRecoveryService;
        this.userAccountService = userAccountService;
    }

    /**
     * Visitor who might be Registered, but not yet logged in, clicks
     * on 'password forgotten' at login formular.
     *
     * @param model Model
     * @return a Formular for entering the email-adress.
     */
    @RequestMapping(path="/form", method = RequestMethod.GET)
    public final String passwordForgottenForm(Model model) {
        UserAccountRegistrationForm userAccountRegistrationForm = new UserAccountRegistrationForm();
        model.addAttribute("userAccountRegistrationForm", userAccountRegistrationForm);
        return "user/resetPassword/resetPasswordForm";
    }

    /**
     * If email-address exists, send email with Link for password-Reset.
     *
     * @param userAccountRegistrationForm UserAccountRegistrationForm
     * @param result BindingResult
     * @param model Model
     * @return info page if without errors or formular again displaying error messages.
     */
    @RequestMapping(path="/form", method = RequestMethod.POST)
    public final String passwordForgottenPost(
        @Valid UserAccountRegistrationForm userAccountRegistrationForm,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            log.info("----------------------");
            log.info(userAccountRegistrationForm.toString());
            log.info(result.toString());
            log.info(model.toString());
            log.info("----------------------");
            return "user/resetPassword/resetPasswordForm";
        } else {
            log.info(userAccountRegistrationForm.toString());
            log.info(result.toString());
            log.info(model.toString());
            if(userAccountService == null){
                return "redirect:/";
            }
            if (userAccountService.findByUserEmail(userAccountRegistrationForm.getEmail()) == null) {
                String objectName = "userRegistrationForm";
                String field = "email";
                String defaultMessage = "This Email is not registered.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/resetPassword/resetPasswordForm";
            } else {
                userAccountPasswordRecoveryService.passwordRecoverySendEmailTo(userAccountRegistrationForm.getEmail());
                return "user/resetPassword/resetPasswordSentMail";
            }
        }
    }

    /**
     * User clicked on Link in Email for Password-Recovery.
     *
     * @param confirmId String
     * @param model Model
     * @return a Formular for entering the new Password.
     */
    @RequestMapping(path = "/confirm/{confirmId}", method = RequestMethod.GET)
    public final String enterNewPasswordFormular(
        @PathVariable String confirmId,
        Model model
    ) {
        UserAccountPasswordRecovery oUserAccountPasswordRecovery =
            userAccountPasswordRecoveryService.findByToken(confirmId);
        if (oUserAccountPasswordRecovery != null) {
            userAccountPasswordRecoveryService.passwordRecoveryClickedInEmail(oUserAccountPasswordRecovery);
            UserAccount ua = userAccountService.findByUserEmail(oUserAccountPasswordRecovery.getEmail());
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(oUserAccountPasswordRecovery.getEmail());
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
     * @param userAccountForm UserAccountForm
     * @param result BindingResult
     * @param confirmId String
     * @param model Model
     *
     * @return Info Page for success or back to formular with error messages.
     */
    @RequestMapping(path =  "/confirm/{confirmId}", method = RequestMethod.POST)
    public final String enterNewPasswordPost(
        @Valid UserAccountForm userAccountForm,
        BindingResult result,
        @PathVariable String confirmId,
        Model model
    ) {
        UserAccountPasswordRecovery o = userAccountPasswordRecoveryService.findByToken(confirmId);
        boolean passwordsMatch = userAccountForm.passwordsAreTheSame();
        if (o != null) {
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.changeUsersPassword(userAccountForm);
                userAccountPasswordRecoveryService.passwordRecoveryDone(o);
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
