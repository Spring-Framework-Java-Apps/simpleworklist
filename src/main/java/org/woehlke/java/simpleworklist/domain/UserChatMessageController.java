package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountChatMessage;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.chat.ChatMessageForm;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.chat.UserChatMessageControllerService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;

/**
 * Created by
 * on 16.02.2016.
 */
@Log
@Controller
@RequestMapping(path = "/user2user")
public class UserChatMessageController extends AbstractController {

  private final UserChatMessageControllerService userChatMessageControllerService;

  private final BreadcrumbService breadcrumbService;

  @Autowired
  public UserChatMessageController(
      UserChatMessageControllerService userChatMessageControllerService,
      BreadcrumbService breadcrumbService
  ) {
    this.userChatMessageControllerService = userChatMessageControllerService;
    this.breadcrumbService = breadcrumbService;
  }

  @RequestMapping(path = "/{userId}/messages/", method = RequestMethod.GET)
  public final String getLastMessagesBetweenCurrentAndOtherUser(
    @Valid @NotNull @PathVariable("userId") UserAccount otherUser,
    @PageableDefault(sort = "rowCreatedAt", direction = Sort.Direction.DESC) Pageable request,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale,
    Model model
  ) {
    log.info("getLastMessagesBetweenCurrentAndOtherUser");
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("Context context");
    Context context = super.getContext(userSession);
    log.info(context.toString());
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("UserAccount thisUser");
    UserAccount thisUser = context.getUserAccount();
    log.info(thisUser.toString());
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("ChatMessageForm chatMessageForm");
    ChatMessageForm chatMessageForm = new ChatMessageForm();
    log.info(chatMessageForm.toString());
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("Page<UserAccountChatMessage> user2UserMessagePage");
    Page<UserAccountChatMessage> user2UserMessagePage =
        userChatMessageControllerService.readAllMessagesBetweenCurrentAndOtherUser(thisUser, otherUser, request);
    for (UserAccountChatMessage o : user2UserMessagePage) {
      log.info(o.toString());
    }
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("breadcrumb");
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale, userSession);
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("model.addAttributes");
    model.addAttribute("newUser2UserMessage", chatMessageForm);
    model.addAttribute("otherUser", otherUser);
    model.addAttribute("user2UserMessagePage", user2UserMessagePage);
    model.addAttribute("refreshMessages", true);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("userSession", userSession);
    log.info("-----------------------------------------------------------------------------------------------");
    log.info("getLastMessagesBetweenCurrentAndOtherUser DONE");
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
    model.addAttribute("userSession", userSession);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForMessagesBetweenCurrentAndOtherUser(locale, userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    if (result.hasErrors()) {
      log.info("result.hasErrors");
      for (ObjectError objectError : result.getAllErrors()) {
        log.info("result.hasErrors: " + objectError.toString());
      }
      Page<UserAccountChatMessage> user2UserMessagePage =
          userChatMessageControllerService.readAllMessagesBetweenCurrentAndOtherUser(thisUser, otherUser, request);
      model.addAttribute("otherUser", otherUser);
      model.addAttribute("user2UserMessagePage", user2UserMessagePage);
      model.addAttribute("userSession", userSession);
      return "user/messages/all";
    } else {
      userChatMessageControllerService.sendNewUserMessage(thisUser, otherUser, chatMessageForm);
      model.addAttribute("userSession", userSession);
      return "redirect:/user2user/" + otherUser.getId() + "/messages/";
    }
  }

}
