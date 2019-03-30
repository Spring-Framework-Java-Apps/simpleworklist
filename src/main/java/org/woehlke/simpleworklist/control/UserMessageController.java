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
import org.woehlke.simpleworklist.entities.UserMessage;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
@Controller
public class UserMessageController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = "/user/{userId}/messages/", method = RequestMethod.GET)
    public final String getLastMessagesBetweenCurrentAndOtherUser(@PathVariable long userId, Model model) {
        UserMessage newUserMessage = new UserMessage();
        UserAccount receiver = userService.retrieveCurrentUser();
        UserAccount sender = super.userService.findUserById(userId);
        List<UserMessage> userMessageList = userMessageService.getLast20MessagesBetweenCurrentAndOtherUser(receiver,sender);
        for(UserMessage userMessage:userMessageList){
            if((!userMessage.isReadByReceiver()) && (receiver.getId().longValue()==userMessage.getReceiver().getId().longValue())){
                userMessage.setReadByReceiver(true);
                userMessageService.update(userMessage);
            }
        }
        model.addAttribute("newUserMessage",newUserMessage);
        model.addAttribute("otherUser",sender);
        model.addAttribute("userMessageList",userMessageList);
        model.addAttribute("refreshMessages",true);
        return "t/pages/userMessages";
    }

    @RequestMapping(value = "/user/{userId}/messages/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            Model model,
            @Valid @ModelAttribute("newUserMessage") UserMessage newUserMessage,
            BindingResult result,
            @PathVariable long userId) {
        LOGGER.info("sendNewMessageToOtherUser");
        UserAccount sender = userService.retrieveCurrentUser();
        UserAccount receiver = super.userService.findUserById(userId);
        if(result.hasErrors()){
            /*
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            */
            LOGGER.info("result.hasErrors");
            List<UserMessage> userMessageList = userMessageService.getLast20MessagesBetweenCurrentAndOtherUser(sender,receiver);
            model.addAttribute("otherUser",receiver);
            model.addAttribute("userMessageList",userMessageList);
            return "t/pages/userMessages";
        } else {
            userMessageService.sendNewUserMessage(sender,receiver,newUserMessage);
            return "redirect:/user/"+userId+"/messages/";
        }
    }

    @RequestMapping(value = "/user/{userId}/messages/all", method = RequestMethod.GET)
    public final String getAllMessagesBetweenCurrentAndOtherUser(@PathVariable long userId, Model model) {
        UserMessage newUserMessage = new UserMessage();
        UserAccount receiver = userService.retrieveCurrentUser();
        UserAccount sender = super.userService.findUserById(userId);
        List<UserMessage> userMessageList = userMessageService.getAllMessagesBetweenCurrentAndOtherUser(receiver,sender);
        for(UserMessage userMessage:userMessageList){
            if((!userMessage.isReadByReceiver()) && (userMessage.getReceiver().getId() == receiver.getId())){
                userMessage.setReadByReceiver(true);
                userMessageService.update(userMessage);
            }
        }
        model.addAttribute("newUserMessage",newUserMessage);
        model.addAttribute("otherUser",sender);
        model.addAttribute("userMessageList",userMessageList);
        return "t/pages/userMessages";
    }

    @RequestMapping(value = "/user/{userId}/messages/all", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser2(
            Model model,
            @Valid @ModelAttribute("newUserMessage") UserMessage newUserMessage,
            BindingResult result,
            @PathVariable long userId) {
        return this.sendNewMessageToOtherUser(model,newUserMessage,result,userId);
    }
}
