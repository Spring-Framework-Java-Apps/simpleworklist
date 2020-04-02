package org.woehlke.simpleworklist.context;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.user.UserSessionBean;

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
    public String switchContxt(@PathVariable("newContextId") Context setContext,
                               @ModelAttribute("userSession") UserSessionBean userSession, Model model){
        Context isContext = super.getContext(userSession);
        if (setContext != null) {
            userSession.setContextId(setContext.getId());
        }
        model.addAttribute("userSession",userSession);
        return "redirect:/taskstate/inbox";
    }
}
