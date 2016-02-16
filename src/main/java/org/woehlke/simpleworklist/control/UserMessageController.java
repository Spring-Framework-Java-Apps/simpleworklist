package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;
import org.woehlke.simpleworklist.services.UserMessageService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
@Controller
public class UserMessageController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionItemController.class);

    @Inject
    private UserMessageService userMessageService;

    @RequestMapping(value = "/user/{userId}/messages/", method = RequestMethod.GET)
    public final String getMessagesBetweenCurrentAndOtherUser(@PathVariable long userId, Model model) {
        UserMessage newUserMessage = new UserMessage();
        UserAccount otherUser = super.userService.findUserById(userId);
        List<UserMessage> userMessageList = userMessageService.getAllMessagesBetweenCurrentAndOtherUser(userId);
        model.addAttribute("newUserMessage",newUserMessage);
        model.addAttribute("otherUser",otherUser);
        model.addAttribute("userMessageList",userMessageList);
        return "pages/userMessages";
    }
}
