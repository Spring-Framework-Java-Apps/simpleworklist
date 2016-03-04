package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;

import java.util.List;
import java.util.Map;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class PagesController extends AbstractController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public final String home() {
        return "redirect:/focus/inbox";
    }

    /**
     * @param model
     * @return List of all registered users.
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public final String getRegisteredUsers(Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        List<UserAccount> users = userService.findAll();
        Map<Long,Integer> usersToNewMessages = userService.getNewIncomingMessagesForEachOtherUser(user);
        model.addAttribute("usersToNewMessages", usersToNewMessages);
        model.addAttribute("users", users);
        model.addAttribute("thisUser", user);
        return "pages/users";
    }
}
