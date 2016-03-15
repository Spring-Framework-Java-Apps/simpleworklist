package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.Language;
import org.woehlke.simpleworklist.model.UserChangeLanguageFormBean;
import org.woehlke.simpleworklist.model.UserChangeNameFormBean;
import org.woehlke.simpleworklist.model.UserChangePasswordFormBean;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Fert on 14.03.2016.
 */
@Controller
public class UserSelfserviceController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSelfserviceController.class);

    @RequestMapping(value = "/user/selfservice", method = RequestMethod.GET)
    public String userProfileAndMenu(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        //TODO: change from List to Page
        List<UserAccount> users = userService.findAll();
        Map<Long,Integer> usersToNewMessages = userService.getNewIncomingMessagesForEachOtherUser(user);
        model.addAttribute("usersToNewMessages", usersToNewMessages);
        model.addAttribute("users", users);
        model.addAttribute("thisUser", user);
        return "user/selfservice/profile";
    }

    @RequestMapping(value = "/user/selfservice/name", method = RequestMethod.GET)
    public String userNameForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        UserChangeNameFormBean bean = new UserChangeNameFormBean(user.getUserFullname());
        model.addAttribute("username", bean);
        model.addAttribute("thisUser", user);
        return "user/selfservice/name";
    }

    @RequestMapping(value = "/user/selfservice/name", method = RequestMethod.POST)
    public String userNameStore(@Valid UserChangeNameFormBean username, BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()) {
            model.addAttribute("username", username);
            model.addAttribute("thisUser", user);
            return "user/selfservice/name";
        } else {
            user.setUserFullname(username.getUserFullname());
            userService.saveAndFlush(user);
            return "redirect:/user/selfservice";
        }
    }

    @RequestMapping(value = "/user/selfservice/password", method = RequestMethod.GET)
    public String userPasswordForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        UserChangePasswordFormBean userChangePasswordFormBean = new UserChangePasswordFormBean();
        model.addAttribute("userChangePasswordFormBean", userChangePasswordFormBean);
        return "user/selfservice/password";
    }

    @RequestMapping(value = "/user/selfservice/password", method = RequestMethod.POST)
    public String userPasswordStore(@Valid UserChangePasswordFormBean userChangePasswordFormBean,
                                   BindingResult result, Model model) {
        LOGGER.info("---------------------------------------------------------");
        LOGGER.info("userPasswordStore");
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()) {
            LOGGER.info("result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/password";
        } else {
            if(! userChangePasswordFormBean.passwordsAreTheSame()){
                LOGGER.info("passwords Are Not The Same");
                String objectName = "userChangePasswordFormBean";
                String field = "userPassword";
                String defaultMessage = "Passwords aren't the same.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                for(ObjectError error : result.getAllErrors()){
                    LOGGER.info(error.toString());
                }
                return "user/selfservice/password";
            }
            if(!userService.confirmUserByLoginAndPassword(user.getUserEmail(), userChangePasswordFormBean.getOldUserPassword())){
                LOGGER.info("old Password is wrong");
                String objectName = "userChangePasswordFormBean";
                String field = "oldUserPassword";
                String defaultMessage = "Password is incorrect";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                for(ObjectError error : result.getAllErrors()){
                    LOGGER.info(error.toString());
                }
                return "user/selfservice/password";
            }
            LOGGER.info("OK");
            userService.changeUsersPassword(userChangePasswordFormBean,user);
            return "redirect:/user/selfservice";
        }
    }

    @RequestMapping(value = "/user/selfservice/areas", method = RequestMethod.GET)
    public String userAreasForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        return "user/selfservice/areas";
    }

    @RequestMapping(value = "/user/selfservice/language", method = RequestMethod.GET)
    public String userLanguageForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        model.addAttribute("languages", Language.list());
        model.addAttribute("userChangeLanguageFormBean",new UserChangeLanguageFormBean(user.getDefaultLanguage()));
        return "user/selfservice/language";
    }

    @RequestMapping(value = "/user/selfservice/language", method = RequestMethod.POST)
    public String userLanguageStore(@Valid UserChangeLanguageFormBean userChangeLanguageFormBean,
                                    BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userLanguageStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/language";
        } else {
            user.setDefaultLanguage(userChangeLanguageFormBean.getDefaultLanguage());
            userService.saveAndFlush(user);
            String returnUrl;
            switch (userChangeLanguageFormBean.getDefaultLanguage()){
                case DE: returnUrl="redirect:/user/selfservice?lang=de"; break;
                default: returnUrl="redirect:/user/selfservice?lang=en"; break;
            }
            return returnUrl;
        }
    }

}
