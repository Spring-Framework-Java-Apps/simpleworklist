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
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationProcessService registrationProcessService;

    /**
     * Register as new user by entering the email-address which is
     * unique and the login identifier.
     *
     * @param model
     * @return Formular for entering Email-Address for Registration
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public final String registerNewUserRequestForm(Model model) {
        RegisterFormBean registerFormBean = new RegisterFormBean();
        model.addAttribute("registerFormBean", registerFormBean);
        return "t/user/registerForm";
    }

    /**
     * Register new User: Store the Request and send Email for Verification.
     *
     * @param registerFormBean
     * @param result
     * @param model
     * @return info page at success or return to form with error messages.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public final String registerNewUserRequestStoreAndSendEmailForVerification(
            @Valid RegisterFormBean registerFormBean,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "t/user/registerForm";
        } else {
            registrationProcessService.registrationCheckIfResponseIsInTime(registerFormBean.getEmail());
            if (userService.isEmailAvailable(registerFormBean.getEmail())) {
                if (registrationProcessService.registrationIsRetryAndMaximumNumberOfRetries(registerFormBean.getEmail())) {
                    String objectName = "registerFormBean";
                    String field = "email";
                    String defaultMessage = "Maximum Number of Retries reached.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                    return "t/user/registerForm";
                } else {
                    registrationProcessService.registrationSendEmailTo(registerFormBean.getEmail());
                    return "t/user/registerSentMail";
                }
            } else {
                String objectName = "registerFormBean";
                String field = "email";
                String defaultMessage = "Email is already in use.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "t/user/registerForm";
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
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if (o != null) {
            registrationProcessService.registrationClickedInEmail(o);
            UserAccountFormBean userAccountFormBean = new UserAccountFormBean();
            userAccountFormBean.setUserEmail(o.getEmail());
            model.addAttribute("userAccountFormBean", userAccountFormBean);
            return "t/user/registerConfirmed";
        } else {
            return "t/user/registerNotConfirmed";
        }
    }

    /**
     * Saving Account Task from Formular and forward to login page.
     *
     * @param userAccountFormBean
     * @param result
     * @param confirmId
     * @param model
     * @return login page at success or page with error messages.
     */
    @RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.POST)
    public final String registerNewUserCheckResponseAndRegistrationStore(
            @PathVariable String confirmId,
            @Valid UserAccountFormBean userAccountFormBean,
            BindingResult result, Model model) {
        LOGGER.info("POST /confirm/" + confirmId + " : " + userAccountFormBean.toString());
        registrationProcessService.registrationCheckIfResponseIsInTime(userAccountFormBean.getUserEmail());
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if (o != null) {
            boolean passwordsMatch = userAccountFormBean.passwordsAreTheSame();
            if (!result.hasErrors() && passwordsMatch) {
                userService.createUser(userAccountFormBean);
                registrationProcessService.registrationUserCreated(o);
                return "t/user/registerDone";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountFormBean";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "t/user/registerConfirmed";
            }
        } else {
            return "t/user/registerNotConfirmed";
        }
    }
}
