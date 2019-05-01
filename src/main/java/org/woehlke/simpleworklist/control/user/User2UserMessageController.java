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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;
import org.woehlke.simpleworklist.model.beans.NewUser2UserMessage;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.entities.User2UserMessage;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by
 * on 16.02.2016.
 */
@Controller
@RequestMapping(value = "/user2user/")
public class User2UserMessageController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = "/{userId}/messages/", method = RequestMethod.GET)
    public final String getLastMessagesBetweenCurrentAndOtherUser(
            @PathVariable("userId") UserAccount otherUser,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        model.addAttribute("userSession",userSession);
        NewUser2UserMessage newUser2UserMessage = new NewUser2UserMessage();
        Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
        model.addAttribute("newUser2UserMessage", newUser2UserMessage);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("user2UserMessagePage", user2UserMessagePage);
        model.addAttribute("refreshMessages",true);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale);
        model.addAttribute("breadcrumb",breadcrumb);
        return "user/messages/all";
    }

    @RequestMapping(value = "/{userId}/messages/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            @PathVariable("userId") UserAccount otherUser,
            @Valid @ModelAttribute("newUser2UserMessage") NewUser2UserMessage newUser2UserMessage,
            BindingResult result,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        LOGGER.info("sendNewMessageToOtherUser");
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        model.addAttribute("userSession",userSession);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale);
        model.addAttribute("breadcrumb",breadcrumb);
        if(result.hasErrors()){
            LOGGER.info("result.hasErrors");
            for(ObjectError objectError:result.getAllErrors()){
                LOGGER.info("result.hasErrors: "+objectError.toString());
            }
            Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
            model.addAttribute("otherUser", otherUser);
            model.addAttribute("user2UserMessagePage", user2UserMessagePage);
            return "user/messages/all";
        } else {
            user2UserMessageService.sendNewUserMessage(thisUser, otherUser, newUser2UserMessage);
            return "redirect:/user2user/"+otherUser.getId()+"/messages/";
        }
    }

}
