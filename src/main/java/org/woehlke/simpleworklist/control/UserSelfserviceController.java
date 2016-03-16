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
import org.woehlke.simpleworklist.entities.Area;
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

    @RequestMapping(value = "/user/selfservice/areas", method = RequestMethod.GET)
    public String userAreasForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        UserChangeDefaultAreaFormBean bean = new UserChangeDefaultAreaFormBean();
        bean.setId(user.getId());
        bean.setDefaultArea(user.getDefaultArea());
        model.addAttribute("thisUser", bean);
        List<Area> areas = areaService.getAllForUser(user);
        model.addAttribute("areas", areas);
        return "user/selfservice/areas";
    }

    @RequestMapping(value = "/user/selfservice/areas", method = RequestMethod.POST)
    public String userAreasSave(@Valid @ModelAttribute("thisUser") UserChangeDefaultAreaFormBean thisUser, BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userAreasSave: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/areas";
        } else {
            if(user.getId() == thisUser.getId()){
                user.setDefaultArea(thisUser.getDefaultArea());
                userService.saveAndFlush(user);
                model.addAttribute("areaId",new UserSessionBean(thisUser.getDefaultArea().getId()));
            }
            return "redirect:/user/selfservice/areas";
        }
    }

    @RequestMapping(value = "/user/selfservice/area/add", method = RequestMethod.GET)
    public String userNewAreaForm(Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        NewAreaFormBean newArea = new NewAreaFormBean();
        model.addAttribute("newArea", newArea);
        return "user/selfservice/areaAdd";
    }

    @RequestMapping(value = "/user/selfservice/area/add", method = RequestMethod.POST)
    public String userNewAreaStore(@Valid NewAreaFormBean newArea, BindingResult result, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userNewAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/areaAdd";
        } else {
            areaService.createNewArea(newArea,user);
            return "redirect:/user/selfservice/areas";
        }
    }

    @RequestMapping(value = "/user/selfservice/area/edit/{areaId}", method = RequestMethod.GET)
    public String userEditAreaForm(@PathVariable long areaId, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        Area area = areaService.findByIdAndUserAccount(areaId,user);
        NewAreaFormBean editArea = new NewAreaFormBean();
        editArea.setNameDe(area.getNameDe());
        editArea.setNameEn(area.getNameEn());
        model.addAttribute("editArea", editArea);
        return "user/selfservice/areaEdit";
    }

    @RequestMapping(value = "/user/selfservice/area/edit/{areaId}", method = RequestMethod.POST)
    public String userEditAreaStore(@Valid NewAreaFormBean editArea, BindingResult result, Model model, @PathVariable long areaId){
        UserAccount user = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            LOGGER.info("userEditAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                LOGGER.info(error.toString());
            }
            return "user/selfservice/areaEdit";
        } else {
            areaService.updateArea(editArea,user, areaId);
            return "redirect:/user/selfservice/areas";
        }
    }

    //TODO: is in session active? -> display message in frontend
    //TODO: has projects or tasks? -> display message in frontend
    @RequestMapping(value = "/user/selfservice/area/delete/{id}", method = RequestMethod.GET)
    public String userDeleteArea(
            @PathVariable long id,
            @ModelAttribute("areaId") UserSessionBean areaId,
            BindingResult result,
            Model model){
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        Area area = areaService.findByIdAndUserAccount(id,user);
        if(areaId.getAreaId() == area.getId()){
            LOGGER.info("area is active in session: "+area);
        } else {
            if(user.getDefaultArea().getId() == area.getId()){
                LOGGER.info("area is default area of this user: "+area);
            } else {
                if(areaService.areaHasItems(area)){
                    LOGGER.info("area has items: "+area);
                } else {
                    boolean deleted = areaService.delete(area);
                    if(!deleted){
                        LOGGER.info("area not deleted: "+area);
                    }
                }
            }
        }
        return "redirect:/user/selfservice/areas";
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
