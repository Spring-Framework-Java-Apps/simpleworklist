package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 13.03.16.
 */
@Log
@Controller
@RequestMapping(path = "/context")
public class ContextController extends AbstractController {

    private final ContextService contextService;

    @Autowired
    public ContextController(ContextService contextService) {
       this.contextService = contextService;
    }

    @RequestMapping(path = "/choose/{newContextId}", method = RequestMethod.GET)
    public String switchContxt(
        @PathVariable("newContextId") Context newContext,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        log.info("switchContxt");
        Context oldContext = super.getContext(userSession);
        if (newContext != null) {
            userSession.setLastContextId(newContext.getId());
        } else {
            userSession.setLastContextId(oldContext.getId());
        }
        model.addAttribute("userSession", userSession);
        return "redirect:/taskstate/inbox";
    }
}
