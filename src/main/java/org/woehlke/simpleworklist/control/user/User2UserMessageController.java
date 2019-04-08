package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.entities.User2UserMessage;

import javax.validation.Valid;

/**
 * Created by Fert on 16.02.2016.
 */
@Controller
@RequestMapping(value = "/user2user/")
public class User2UserMessageController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = "/{userId}/messages/", method = RequestMethod.GET)
    public final String getLastMessagesBetweenCurrentAndOtherUser(
            @PathVariable long userId,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
            Model model
    ) {
        User2UserMessage newUser2UserMessage = new User2UserMessage();
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        UserAccount otherUser = super.userAccountService.findUserById(userId);
        Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
        model.addAttribute("newUser2UserMessage", newUser2UserMessage);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("user2UserMessagePage", user2UserMessagePage);
        model.addAttribute("refreshMessages",true);
        return "user/messages/all";
    }

    @RequestMapping(value = "/{userId}/messages/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            Model model,
            @Valid @ModelAttribute("newUserMessage") User2UserMessage newUser2UserMessage,
            BindingResult result,
            @PathVariable long userId,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request) {
        LOGGER.info("sendNewMessageToOtherUser");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        UserAccount otherUser = super.userAccountService.findUserById(userId);
        if(result.hasErrors()){
            LOGGER.info("result.hasErrors");
            Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
            model.addAttribute("otherUser", otherUser);
            model.addAttribute("user2UserMessagePage", user2UserMessagePage);
            return "user/messages/all";
        } else {
            user2UserMessageService.sendNewUserMessage(thisUser, otherUser, newUser2UserMessage);
            return "redirect:/user2user/"+userId+"/messages/";
        }
    }

}
