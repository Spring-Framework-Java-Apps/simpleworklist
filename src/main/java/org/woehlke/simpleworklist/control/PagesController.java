package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;

import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class PagesController extends AbstractController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public final String home(Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        model.addAttribute("username",user.getUserFullname());
        return "pages/home";
    }

    /**
     * @param model
     * @return List of all registered users.
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public final String getRegisteredUsers(Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        List<UserAccount> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("thisUser", user);
        return "pages/users";
    }
}
