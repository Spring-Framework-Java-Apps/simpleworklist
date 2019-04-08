package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.Language;
import org.woehlke.simpleworklist.model.beans.*;
import org.woehlke.simpleworklist.model.services.UserAccountAccessService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Fert on 14.03.2016.
 */
@Controller
@RequestMapping(value = "/user/selfservice")
public class UserSelfserviceController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSelfserviceController.class);


    private final UserAccountAccessService userAccountAccessService;

    @Autowired
    public UserSelfserviceController(UserAccountAccessService userAccountAccessService) {
        this.userAccountAccessService = userAccountAccessService;
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String userProfileAndMenu(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        //TODO: change from List to Page
        List<UserAccount> users = userAccountService.findAll();
        for(UserAccount u:users){
            LOGGER.info(u.getUserFullname()+": "+u.getUserEmail());
        }
        Map<Long,Integer> usersToNewMessages = userAccountService.getNewIncomingMessagesForEachOtherUser(user);
        model.addAttribute("usersToNewMessages", usersToNewMessages);
        model.addAttribute("users", users);
        model.addAttribute("thisUser", user);
        return "user/selfservice/profile";
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String userNameForm(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        UserChangeNameForm bean = new UserChangeNameForm(user.getUserFullname());
        model.addAttribute("username", bean);
        model.addAttribute("thisUser", user);
        return "user/selfservice/name";
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    public String userNameStore(@Valid UserChangeNameForm username, BindingResult result, Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()) {
            model.addAttribute("username", username);
            model.addAttribute("thisUser", user);
            return "user/selfservice/name";
        } else {
            user.setUserFullname(username.getUserFullname());
            userAccountService.saveAndFlush(user);
            return "redirect:/user/selfservice/profile";
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String userPasswordForm(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        UserChangePasswordForm userChangePasswordForm = new UserChangePasswordForm();
        model.addAttribute("userChangePasswordForm", userChangePasswordForm);
        return "user/selfservice/password";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String userPasswordStore(@Valid UserChangePasswordForm userChangePasswordForm,
                                   BindingResult result, Model model) {
        LOGGER.info("---------------------------------------------------------");
        LOGGER.info("userPasswordStore");
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()) {
            LOGGER.info("result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/password";
        } else {
            if(! userChangePasswordForm.passwordsAreTheSame()){
                LOGGER.info("passwords Are Not The Same");
                String objectName = "userChangePasswordForm";
                String field = "userPassword";
                String defaultMessage = "Passwords aren't the same.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                for(ObjectError error : result.getAllErrors()){
                    LOGGER.info(error.toString());
                }
                return "user/selfservice/password";
            }
            if(!userAccountAccessService.confirmUserByLoginAndPassword(user.getUserEmail(), userChangePasswordForm.getOldUserPassword())){
                LOGGER.info("old Password is wrong");
                String objectName = "userChangePasswordForm";
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
            userAccountAccessService.changeUsersPassword(userChangePasswordForm,user);
            return "redirect:/user/selfservice/profile";
        }
    }

    @RequestMapping(value = "/contexts", method = RequestMethod.GET)
    public String userAreasForm(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        UserChangeDefaultContextForm bean = new UserChangeDefaultContextForm();
        bean.setId(user.getId());
        bean.setDefaultContext(user.getDefaultContext());
        model.addAttribute("thisUser", bean);
        List<Context> contexts = contextService.getAllForUser(user);
        model.addAttribute("contexts", contexts);
        return "user/selfservice/context/all";
    }

    @RequestMapping(value = "/contexts", method = RequestMethod.POST)
    public String userAreasSave(@Valid @ModelAttribute("thisUser") UserChangeDefaultContextForm thisUser, BindingResult result, Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userAreasSave: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/context/all";
        } else {
            if(user.getId() == thisUser.getId()){
                user.setDefaultContext(thisUser.getDefaultContext());
                userAccountService.saveAndFlush(user);
                model.addAttribute("userSession",new UserSessionBean(thisUser.getDefaultContext().getId()));
            }
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(value = "/context/add", method = RequestMethod.GET)
    public String userNewAreaForm(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        NewContextForm newContext = new NewContextForm();
        model.addAttribute("newContext", newContext);
        return "user/selfservice/context/add";
    }

    @RequestMapping(value = "/context/add", method = RequestMethod.POST)
    public String userNewAreaStore(@Valid NewContextForm newContext, BindingResult result, Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userNewAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/context/add";
        } else {
            contextService.createNewContext(newContext,user);
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(value = "/context/edit/{contextId}", method = RequestMethod.GET)
    public String userEditAreaForm(@PathVariable long contextId, Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        Context context = contextService.findByIdAndUserAccount(contextId,user);
        NewContextForm editContext = new NewContextForm();
        editContext.setNameDe(context.getNameDe());
        editContext.setNameEn(context.getNameEn());
        model.addAttribute("editContext", editContext);
        return "user/selfservice/context/edit";
    }

    @RequestMapping(value = "/context/edit/{contextId}", method = RequestMethod.POST)
    public String userEditAreaStore(@Valid NewContextForm editContext, BindingResult result, Model model, @PathVariable long contextId){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userEditAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/context/edit";
        } else {
            contextService.updateContext(editContext,user, contextId);
            return "redirect:/user/selfservice/contexts";
        }
    }

    //TODO: is in session active? -> display message in frontend
    //TODO: has projects or tasks? -> display message in frontend
    @RequestMapping(value = "/context/delete/{id}", method = RequestMethod.GET)
    public String userDeleteArea(
            @PathVariable long id,
            @ModelAttribute("userSession") UserSessionBean userSession,
            BindingResult result,
            Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        Context context = contextService.findByIdAndUserAccount(id,user);
        if(userSession.getContextId() == context.getId()){
            LOGGER.info("context is active in session: "+ context);
        } else {
            if(user.getDefaultContext().getId() == context.getId()){
                LOGGER.info("context is default context of this user: "+ context);
            } else {
                if(contextService.contextHasItems(context)){
                    LOGGER.info("context has items: "+ context);
                } else {
                    boolean deleted = contextService.delete(context);
                    if(!deleted){
                        LOGGER.info("context not deleted: "+ context);
                    }
                }
            }
        }
        return "redirect:/user/selfservice/contexts";
    }

    @RequestMapping(value = "/language", method = RequestMethod.GET)
    public String userLanguageForm(Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        model.addAttribute("languages", Language.list());
        model.addAttribute("userChangeLanguageForm",new UserChangeLanguageForm(user.getDefaultLanguage()));
        return "user/selfservice/language";
    }

    @RequestMapping(value = "/language", method = RequestMethod.POST)
    public String userLanguageStore(@Valid UserChangeLanguageForm userChangeLanguageForm,
                                    BindingResult result, Model model){
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userLanguageStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/language";
        } else {
            user.setDefaultLanguage(userChangeLanguageForm.getDefaultLanguage());
            userAccountService.saveAndFlush(user);
            String returnUrl;
            switch (userChangeLanguageForm.getDefaultLanguage()){
                case DE: returnUrl="redirect:/user/selfservice/profile?lang=de"; break;
                default: returnUrl="redirect:/user/selfservice/profile?lang=en"; break;
            }
            return returnUrl;
        }
    }

}
