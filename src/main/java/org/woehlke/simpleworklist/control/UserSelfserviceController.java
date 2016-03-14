package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserNameFormBean;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Fert on 14.03.2016.
 */
@Controller
public class UserSelfserviceController extends AbstractController {

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
        UserNameFormBean bean = new UserNameFormBean(user.getUserFullname());
        model.addAttribute("username", bean);
        model.addAttribute("thisUser", user);
        return "user/selfservice/name";
    }

    @RequestMapping(value = "/user/selfservice/name", method = RequestMethod.POST)
    public String userNameStore(@Valid UserNameFormBean username, BindingResult result, Model model){
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
        return "user/selfservice/password";
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
        return "user/selfservice/language";
    }

}
