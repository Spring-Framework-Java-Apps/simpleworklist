package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
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
import org.woehlke.java.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.chat.ChatMessage;
import org.woehlke.java.simpleworklist.domain.chat.ChatMessageForm;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.java.simpleworklist.domain.session.UserSessionBean;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by
 * on 16.02.2016.
 */
@Slf4j
@Controller
@RequestMapping(path = "/user2user")
public class ChatMessageController extends AbstractController {

    @RequestMapping(path = "/{userId}/messages/", method = RequestMethod.GET)
    public final String getLastMessagesBetweenCurrentAndOtherUser(
            @PathVariable("userId") UserAccount otherUser,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale,
            Model model
    ) {
        log.info("getLastMessagesBetweenCurrentAndOtherUser");
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        model.addAttribute("userSession",userSession);
        ChatMessageForm chatMessageForm = new ChatMessageForm();
        Page<ChatMessage> user2UserMessagePage = chatMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
        model.addAttribute("newUser2UserMessage", chatMessageForm);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("user2UserMessagePage", user2UserMessagePage);
        model.addAttribute("refreshMessages",true);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale,userSession);
        model.addAttribute("breadcrumb",breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/messages/all";
    }

    @RequestMapping(path = "/{userId}/messages/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            @PathVariable("userId") UserAccount otherUser,
            @Valid @ModelAttribute("newUser2UserMessage") ChatMessageForm chatMessageForm,
            BindingResult result,
            @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale,
            Model model
    ) {
        log.info("sendNewMessageToOtherUser");
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        model.addAttribute("userSession",userSession);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale,userSession);
        model.addAttribute("breadcrumb",breadcrumb);
        if(result.hasErrors()){
            log.info("result.hasErrors");
            for(ObjectError objectError:result.getAllErrors()){
                log.info("result.hasErrors: "+objectError.toString());
            }
            Page<ChatMessage> user2UserMessagePage = chatMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
            model.addAttribute("otherUser", otherUser);
            model.addAttribute("user2UserMessagePage", user2UserMessagePage);
            model.addAttribute("userSession", userSession);
            return "user/messages/all";
        } else {
            chatMessageService.sendNewUserMessage(thisUser, otherUser, chatMessageForm);
            model.addAttribute("userSession", userSession);
            return "redirect:/user2user/" + otherUser.getId() + "/messages/";
        }
    }

}
