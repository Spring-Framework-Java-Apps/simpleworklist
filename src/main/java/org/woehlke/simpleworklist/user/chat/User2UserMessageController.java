package org.woehlke.simpleworklist.user.chat;

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
import org.woehlke.simpleworklist.application.common.AbstractController;
import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.user.session.UserSessionBean;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by
 * on 16.02.2016.
 */
@Log
@Controller
@RequestMapping(path = "/user/messages/")
public class User2UserMessageController extends AbstractController {

    @RequestMapping(path = "/{userId}/", method = RequestMethod.GET)
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
        User2UserMessageFormBean user2UserMessageFormBean = new User2UserMessageFormBean();
        Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
        model.addAttribute("newUser2UserMessage", user2UserMessageFormBean);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("user2UserMessagePage", user2UserMessagePage);
        model.addAttribute("refreshMessages",true);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale);
        model.addAttribute("breadcrumb",breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/messages/all";
    }

    @RequestMapping(path = "/{userId}/", method = RequestMethod.POST)
    public final String sendNewMessageToOtherUser(
            @PathVariable("userId") UserAccount otherUser,
            @Valid @ModelAttribute("newUser2UserMessage") User2UserMessageFormBean user2UserMessageFormBean,
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale);
        model.addAttribute("breadcrumb",breadcrumb);
        if(result.hasErrors()){
            log.info("result.hasErrors");
            for(ObjectError objectError:result.getAllErrors()){
                log.info("result.hasErrors: "+objectError.toString());
            }
            Page<User2UserMessage> user2UserMessagePage = user2UserMessageService.readAllMessagesBetweenCurrentAndOtherUser(thisUser,otherUser,request);
            model.addAttribute("otherUser", otherUser);
            model.addAttribute("user2UserMessagePage", user2UserMessagePage);
            model.addAttribute("userSession", userSession);
            return "user/messages/all";
        } else {
            user2UserMessageService.sendNewUserMessage(thisUser, otherUser, user2UserMessageFormBean);
            model.addAttribute("userSession", userSession);
            return "redirect:/user/messages/" + otherUser.getId();
        }
    }

}
