package org.woehlke.simpleworklist.control.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.entities.services.ContextService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 13.03.16.
 */
@Controller
@RequestMapping(value = "/context")
public class ContextController extends AbstractController {

    private final ContextService contextService;

    @Autowired
    public ContextController(ContextService contextService) {
       this.contextService = contextService;
    }

    @RequestMapping(value = "/choose/{newContextId}", method = RequestMethod.GET)
    public String switchArea(@PathVariable long newContextId, Model model){
        if(newContextId > 0) {
            UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
            Context foundContext = contextService.findByIdAndUserAccount(newContextId, userAccount);
            if (foundContext == null) {
                return "redirect:/logout";
            } else {
                model.addAttribute("userSession", new UserSessionBean(foundContext.getId()));
            }
        } else {
            model.addAttribute("userSession",new UserSessionBean(newContextId));
        }
        return "redirect:/taskstate/inbox";
    }
}