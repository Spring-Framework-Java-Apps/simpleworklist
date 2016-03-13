package org.woehlke.simpleworklist.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskEnergy;
import org.woehlke.simpleworklist.entities.enumerations.TaskTime;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.AreaService;
import org.woehlke.simpleworklist.services.ProjectService;
import org.woehlke.simpleworklist.services.UserMessageService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@SessionAttributes("areaId")
public abstract class AbstractController {

    @Value("${mvc.controller.pageSize}")
    protected int pageSize;

    @Inject
    protected ProjectService projectService;

    @Inject
    protected UserService userService;

    @Inject
    protected UserMessageService userMessageService;

    @Inject
    protected AreaService areaService;

    @ModelAttribute("allCategories")
    public final List<Project> getAllCategories(@ModelAttribute("areaId") UserSessionBean areaId,
                                                BindingResult result, Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        if (areaId.getAreaId() == 0) {
            return projectService.findAllProjectsByUserAccount(user);
        } else {
            Area area = areaService.findByIdAndUserAccount(areaId.getAreaId(), user);
            return projectService.findAllProjectsByUserAccountAndArea(user,area);
        }
    }

    @ModelAttribute("rootCategories")
    public final List<Project> getRootCategories(@ModelAttribute("areaId") UserSessionBean areaId,
                                                 BindingResult result, Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        if (areaId.getAreaId() == 0) {
            return projectService.findRootProjectsByUserAccount(user);
        } else {
            Area area = areaService.findByIdAndUserAccount(areaId.getAreaId(), user);
            return projectService.findRootProjectsByUserAccountAndArea(user,area);
        }
    }

    @ModelAttribute("numberOfNewIncomingMessages")
    public final int getNumberOfNewIncomingMessages(){
        UserAccount user = userService.retrieveCurrentUser();
        return userMessageService.getNumberOfNewIncomingMessagesForUser(user);
    }

    @ModelAttribute("listTaskEnergy")
    public final List<TaskEnergy> getListTaskEnergy(){
        return TaskEnergy.list();
    }

    @ModelAttribute("listTaskTime")
    public final List<TaskTime> getListTaskTime(){
        return TaskTime.list();
    }

    @ModelAttribute("areas")
    public final List<Area> getAreas(){
        UserAccount user = userService.retrieveCurrentUser();
        return areaService.getAllForUser(user);
    }

    @ModelAttribute("area")
    public final String getCurrentArea(@ModelAttribute("areaId") UserSessionBean areaId,
                                       BindingResult result, Model model){
        //TODO: i18n
        String retVal = "all";
        if(!result.hasErrors()){
            UserAccount user = userService.retrieveCurrentUser();
            if (areaId.getAreaId() > 0) {
                Area found = areaService.findByIdAndUserAccount(areaId.getAreaId(), user);
                if(found != null){
                    retVal = found.getName();
                }
            }
        }
        return retVal;
    }

}
