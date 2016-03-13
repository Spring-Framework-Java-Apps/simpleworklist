package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.AreaService;

import javax.inject.Inject;

/**
 * Created by tw on 13.03.16.
 */
@Controller
public class AreaController extends AbstractController {

    @Inject
    private AreaService areaService;

    @RequestMapping(value = "/area/choose/{newAreaId}", method = RequestMethod.GET)
    public String switchArea(@PathVariable long newAreaId, Model model){
        if(newAreaId != 0) {
            UserAccount userAccount = userService.retrieveCurrentUser();
            Area foundArea = areaService.findByIdAndUserAccount(newAreaId, userAccount);
            if (foundArea == null) {
                return "redirect:/logout";
            } else {
                model.addAttribute("areaId", new UserSessionBean(foundArea.getId()));
            }
        } else {
            model.addAttribute("areaId",new UserSessionBean(newAreaId));
        }
        return "redirect:/tasks/inbox";
    }
}
