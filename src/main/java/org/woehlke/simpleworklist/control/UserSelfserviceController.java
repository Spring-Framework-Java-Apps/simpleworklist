package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.Language;
import org.woehlke.simpleworklist.model.*;

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

    @RequestMapping(value = "/user/selfservice/contexts", method = RequestMethod.GET)
    public String userAreasForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        UserChangeDefaultContextFormBean bean = new UserChangeDefaultContextFormBean();
        bean.setId(user.getId());
        bean.setDefaultContext(user.getDefaultContext());
        model.addAttribute("thisUser", bean);
        List<Context> contexts = contextService.getAllForUser(user);
        model.addAttribute("contexts", contexts);
        return "user/selfservice/contexts";
    }

    @RequestMapping(value = "/user/selfservice/contexts", method = RequestMethod.POST)
    public String userAreasSave(@Valid @ModelAttribute("thisUser") UserChangeDefaultContextFormBean thisUser, BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userAreasSave: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/contexts";
        } else {
            if(user.getId() == thisUser.getId()){
                user.setDefaultContext(thisUser.getDefaultContext());
                userService.saveAndFlush(user);
                model.addAttribute("userSession",new UserSessionBean(thisUser.getDefaultContext().getId()));
            }
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(value = "/user/selfservice/context/add", method = RequestMethod.GET)
    public String userNewAreaForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        NewContextFormBean newContext = new NewContextFormBean();
        model.addAttribute("newContext", newContext);
        return "user/selfservice/contextAdd";
    }

    @RequestMapping(value = "/user/selfservice/context/add", method = RequestMethod.POST)
    public String userNewAreaStore(@Valid NewContextFormBean newContext, BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userNewAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/contextAdd";
        } else {
            contextService.createNewContext(newContext,user);
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(value = "/user/selfservice/context/edit/{contextId}", method = RequestMethod.GET)
    public String userEditAreaForm(@PathVariable long contextId, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        Context context = contextService.findByIdAndUserAccount(contextId,user);
        NewContextFormBean editContext = new NewContextFormBean();
        editContext.setNameDe(context.getNameDe());
        editContext.setNameEn(context.getNameEn());
        model.addAttribute("editContext", editContext);
        return "user/selfservice/contextEdit";
    }

    @RequestMapping(value = "/user/selfservice/context/edit/{contextId}", method = RequestMethod.POST)
    public String userEditAreaStore(@Valid NewContextFormBean editContext, BindingResult result, Model model, @PathVariable long contextId){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userEditAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/contextEdit";
        } else {
            contextService.updateContext(editContext,user, contextId);
            return "redirect:/user/selfservice/contexts";
        }
    }

    //TODO: is in session active? -> display message in frontend
    //TODO: has projects or tasks? -> display message in frontend
    @RequestMapping(value = "/user/selfservice/context/delete/{id}", method = RequestMethod.GET)
    public String userDeleteArea(
            @PathVariable long id,
            @ModelAttribute("userSession") UserSessionBean userSession,
            BindingResult result,
            Model model){
        UserAccount user = userService.retrieveCurrentUser();
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
