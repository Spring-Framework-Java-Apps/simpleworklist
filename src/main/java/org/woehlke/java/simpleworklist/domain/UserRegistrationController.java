package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountService;
import org.woehlke.java.simpleworklist.domain.db.user.account.UserAccountForm;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountRegistration;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationForm;
import org.woehlke.java.simpleworklist.domain.db.user.signup.UserAccountRegistrationService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(path = "/user/register")
public class UserRegistrationController {

    private final UserAccountRegistrationService userAccountRegistrationService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserRegistrationController(
        UserAccountRegistrationService userAccountRegistrationService,
        UserAccountService userAccountService
    ) {
        this.userAccountRegistrationService = userAccountRegistrationService;
        this.userAccountService = userAccountService;
    }

    /**
     * Register as new user by entering the email-address which is
     * unique and the login identifier.
     *
     * @param model Model
     * @return Formular for entering Email-Address for Registration
     */
    @RequestMapping(path = "/form", method = RequestMethod.GET)
    public final String registerGet(Model model) {
        log.info("registerGet");
        UserAccountRegistrationForm userAccountRegistrationForm = new UserAccountRegistrationForm();
        model.addAttribute("userAccountRegistrationForm", userAccountRegistrationForm);
        return "user/register/registerForm";
    }

    /**
     * Register new User: Store the Request and send Email for Verification.
     *
     * @param userAccountRegistrationForm UserRegistrationForm
     * @param result BindingResult
     * @param model Model
     * @return info page at success or return to form with error messages.
     */
    @RequestMapping(path = "/form", method = RequestMethod.POST)
    public final String registerPost(
            @Valid UserAccountRegistrationForm userAccountRegistrationForm,
            BindingResult result,
            Model model
    ) {
        log.info("registerPost");
        if (result.hasErrors()) {
            return "user/register/registerForm";
        } else {
            userAccountRegistrationService.registrationCheckIfResponseIsInTime(userAccountRegistrationForm.getEmail());
            if (userAccountService.isEmailAvailable(userAccountRegistrationForm.getEmail())) {
                boolean registrationIsRetryAndMaximumNumberOfRetries =
                    userAccountRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(
                        userAccountRegistrationForm.getEmail()
                    );
                if (registrationIsRetryAndMaximumNumberOfRetries) {
                    String objectName = "userAccountRegistrationForm";
                    String field = "email";
                    String defaultMessage = "Maximum Number of Retries reached.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                    return "user/register/registerForm";
                } else {
                    userAccountRegistrationService.registrationSendEmailTo(userAccountRegistrationForm.getEmail());
                    return "user/register/registerConfirmSentMail";
                }
            } else {
                String objectName = "userAccountRegistrationForm";
                String field = "email";
                String defaultMessage = "Email is already in use.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/register/registerForm";
            }
        }
    }

    /**
     * Register as new user: The URL in the Verification Email clicked by User.
     *
     * @param confirmId String
     * @param model Model
     * @return Formular for Entering Account Task or Error Messages.
     */
    @RequestMapping(path = "/confirm/{confirmId}", method = RequestMethod.GET)
    public final String registerConfirmGet(
        @PathVariable String confirmId,
        Model model
    ) {
        log.info("registerConfirmGet");
        log.info("GET /confirm/" + confirmId);
        UserAccountRegistration o = userAccountRegistrationService.findByToken(confirmId);
        if (o != null) {
            userAccountRegistrationService.registrationClickedInEmail(o);
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(o.getEmail());
            model.addAttribute("userAccountForm", userAccountForm);
            return "user/register/registerConfirmForm";
        } else {
            return "user/register/registerConfirmFailed";
        }
    }

    /**
     * Saving Account Task from Formular and forward to login page.
     *
     * @param userAccountForm UserAccountForm
     * @param result BindingResult
     * @param confirmId String
     * @param model Model
     * @return login page at success or page with error messages.
     */
    @RequestMapping(path = "/confirm/{confirmId}", method = RequestMethod.POST)
    public final String registerConfirmPost(
        @PathVariable String confirmId,
        @Valid UserAccountForm userAccountForm,
        BindingResult result,
        Model model
    ) {
        log.info("registerConfirmPost");
        log.info("POST /confirm/" + confirmId + " : " + userAccountForm.toString());
        userAccountRegistrationService.registrationCheckIfResponseIsInTime(userAccountForm.getUserEmail());
        UserAccountRegistration oUserAccountRegistration = userAccountRegistrationService.findByToken(confirmId);
        if (oUserAccountRegistration != null) {
            boolean passwordsMatch = userAccountForm.passwordsAreTheSame();
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.createUser(userAccountForm);
                userAccountRegistrationService.registrationUserCreated(oUserAccountRegistration);
                return "user/register/registerConfirmFinished";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountRegistrationForm";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/register/registerConfirmForm";
            }
        } else {
            return "user/register/registerConfirmFailed";
        }
    }
}
