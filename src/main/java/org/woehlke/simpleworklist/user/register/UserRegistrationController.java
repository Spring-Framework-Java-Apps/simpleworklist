package org.woehlke.simpleworklist.user.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.user.UserAccountForm;
import org.woehlke.simpleworklist.oodm.entities.UserRegistration;
import org.woehlke.simpleworklist.oodm.services.UserRegistrationService;
import org.woehlke.simpleworklist.oodm.services.UserAccountService;

import javax.validation.Valid;

@Controller
public class UserRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserAccountService userAccountService;

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserAccountService userAccountService, UserRegistrationService userRegistrationService) {
        this.userAccountService = userAccountService;
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * Register as new user by entering the email-address which is
     * unique and the login identifier.
     *
     * @param model
     * @return Formular for entering Email-Address for Registration
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public final String registerNewUserRequestForm(Model model) {
        UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
        model.addAttribute("userRegistrationForm", userRegistrationForm);
        return "user/register/registerForm";
    }

    /**
     * Register new User: Store the Request and send Email for Verification.
     *
     * @param userRegistrationForm
     * @param result
     * @param model
     * @return info page at success or return to form with error messages.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public final String registerNewUserRequestStoreAndSendEmailForVerification(
            @Valid UserRegistrationForm userRegistrationForm,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/register/registerForm";
        } else {
            userRegistrationService.registrationCheckIfResponseIsInTime(userRegistrationForm.getEmail());
            if (userAccountService.isEmailAvailable(userRegistrationForm.getEmail())) {
                if (userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(userRegistrationForm.getEmail())) {
                    String objectName = "userRegistrationForm";
                    String field = "email";
                    String defaultMessage = "Maximum Number of Retries reached.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                    return "user/register/registerForm";
                } else {
                    userRegistrationService.registrationSendEmailTo(userRegistrationForm.getEmail());
                    return "user/register/registerSentMail";
                }
            } else {
                String objectName = "userRegistrationForm";
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
     * @param confirmId
     * @param model
     * @return Formular for Entering Account Task or Error Messages.
     */
    @RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.GET)
    public final String registerNewUserCheckResponseAndRegistrationForm(
            @PathVariable String confirmId, Model model) {
        LOGGER.info("GET /confirm/" + confirmId);
        UserRegistration o = userRegistrationService.findByToken(confirmId);
        if (o != null) {
            userRegistrationService.registrationClickedInEmail(o);
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(o.getEmail());
            model.addAttribute("userAccountForm", userAccountForm);
            return "user/register/registerConfirmed";
        } else {
            return "user/register/registerNotConfirmed";
        }
    }

    /**
     * Saving Account Task from Formular and forward to login page.
     *
     * @param userAccountForm
     * @param result
     * @param confirmId
     * @param model
     * @return login page at success or page with error messages.
     */
    @RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.POST)
    public final String registerNewUserCheckResponseAndRegistrationStore(
            @PathVariable String confirmId,
            @Valid UserAccountForm userAccountForm,
            BindingResult result, Model model) {
        LOGGER.info("POST /confirm/" + confirmId + " : " + userAccountForm.toString());
        userRegistrationService.registrationCheckIfResponseIsInTime(userAccountForm.getUserEmail());
        UserRegistration o = userRegistrationService.findByToken(confirmId);
        if (o != null) {
            boolean passwordsMatch = userAccountForm.passwordsAreTheSame();
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.createUser(userAccountForm);
                userRegistrationService.registrationUserCreated(o);
                return "user/register/registerDone";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountForm";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/register/registerConfirmed";
            }
        } else {
            return "user/register/registerNotConfirmed";
        }
    }
}
