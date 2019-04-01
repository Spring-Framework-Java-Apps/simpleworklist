package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.impl.AbstractController;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.User2UserMessage;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
@Controller
public class User2UserMessageController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = "/user/{userId}/messages/", method = RequestMethod.GET)
    public final String getLastMessagesBetweenCurrentAndOtherUser(@PathVariable long userId, Model model) {
        User2UserMessage newUser2UserMessage = new User2UserMessage();
        UserAccount receiver = userAccountAccessService.retrieveCurrentUser();
        UserAccount sender = super.userAccountService.findUserById(userId);
        List<User2UserMessage> user2UserMessageList = user2UserMessageService.getLast20MessagesBetweenCurrentAndOtherUser(receiver,sender);
        for(User2UserMessage user2UserMessage : user2UserMessageList){
            if((!user2UserMessage.isReadByReceiver()) && (receiver.getId().longValue()== user2UserMessage.getReceiver().getId().longValue())){
                user2UserMessage.setReadByReceiver(true);
                user2UserMessageService.update(user2UserMessage);
            }
        }
        model.addAttribute("newUserMessage", newUser2UserMessage);
        model.addAttribute("otherUser",sender);
        model.addAttribute("userMessageList", user2UserMessageList);
        model.addAttribute("refreshMessages",true);
        return "pages/userMessages";
    }

    @RequestMapping(value = "/user/{userId}/messages/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            Model model,
            @Valid @ModelAttribute("newUserMessage") User2UserMessage newUser2UserMessage,
            BindingResult result,
            @PathVariable long userId) {
        LOGGER.info("sendNewMessageToOtherUser");
        UserAccount sender = userAccountAccessService.retrieveCurrentUser();
        UserAccount receiver = super.userAccountService.findUserById(userId);
        if(result.hasErrors()){
            /*
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            */
            LOGGER.info("result.hasErrors");
            List<User2UserMessage> user2UserMessageList = user2UserMessageService.getLast20MessagesBetweenCurrentAndOtherUser(sender,receiver);
            model.addAttribute("otherUser",receiver);
            model.addAttribute("userMessageList", user2UserMessageList);
            return "pages/userMessages";
        } else {
            user2UserMessageService.sendNewUserMessage(sender,receiver, newUser2UserMessage);
            return "redirect:/user/"+userId+"/messages/";
        }
    }

    @RequestMapping(value = "/user/{userId}/messages/all", method = RequestMethod.GET)
    public final String getAllMessagesBetweenCurrentAndOtherUser(@PathVariable long userId, Model model) {
        User2UserMessage newUser2UserMessage = new User2UserMessage();
        UserAccount receiver = userAccountAccessService.retrieveCurrentUser();
        UserAccount sender = super.userAccountService.findUserById(userId);
        List<User2UserMessage> user2UserMessageList = user2UserMessageService.getAllMessagesBetweenCurrentAndOtherUser(receiver,sender);
        for(User2UserMessage user2UserMessage : user2UserMessageList){
            if((!user2UserMessage.isReadByReceiver()) && (user2UserMessage.getReceiver().getId() == receiver.getId())){
                user2UserMessage.setReadByReceiver(true);
                user2UserMessageService.update(user2UserMessage);
            }
        }
        model.addAttribute("newUserMessage", newUser2UserMessage);
        model.addAttribute("otherUser",sender);
        model.addAttribute("userMessageList", user2UserMessageList);
        return "pages/userMessages";
    }

    @RequestMapping(value = "/user/{userId}/messages/all", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser2(
            Model model,
            @Valid @ModelAttribute("newUserMessage") User2UserMessage newUser2UserMessage,
            BindingResult result,
            @PathVariable long userId) {
        return this.sendNewMessageToOtherUser(model, newUser2UserMessage,result,userId);
    }
}
