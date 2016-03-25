package org.woehlke.simpleworklist.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskEnergy;
import org.woehlke.simpleworklist.entities.enumerations.TaskTime;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.ContextService;
import org.woehlke.simpleworklist.services.ProjectService;
import org.woehlke.simpleworklist.services.UserMessageService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

/**
 * Created by tw on 14.02.16.
 */
@SessionAttributes("userSession")
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
    protected ContextService contextService;

    @ModelAttribute("allCategories")
    public final List<Project> getAllCategories(@ModelAttribute("userSession") UserSessionBean userSession,
                                                BindingResult result, Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        if ((userSession.getContextId() == null)||(userSession.getContextId() == 0)) {
            return projectService.findAllProjectsByUserAccount(user);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), user);
            return projectService.findAllProjectsByUserAccountAndContext(user, context);
        }
    }

    @ModelAttribute("rootCategories")
    public final List<Project> getRootCategories(@ModelAttribute("userSession") UserSessionBean userSession,
                                                 BindingResult result, Model model) {
        UserAccount user = userService.retrieveCurrentUser();
        if ((userSession.getContextId() == null)||(userSession.getContextId() == 0)) {
            return projectService.findRootProjectsByUserAccount(user);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), user);
            return projectService.findRootProjectsByUserAccountAndContext(user, context);
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

    @ModelAttribute("contexts")
    public final List<Context> getContexts(){
        UserAccount user = userService.retrieveCurrentUser();
        return contextService.getAllForUser(user);
    }

    @ModelAttribute("context")
    public final String getCurrentArea(@ModelAttribute("userSession") UserSessionBean userSession,
                                       BindingResult result, Locale locale, Model model){
        UserAccount user = userService.retrieveCurrentUser();
        String retVal = "All";
        if(locale.getLanguage().equalsIgnoreCase("de")){
            retVal = "Alle";
        }
        if (userSession.getContextId() == null) {
            model.addAttribute("userSession", new UserSessionBean(user.getDefaultContext().getId()));
            if (locale.getLanguage().equalsIgnoreCase("de")) {
                retVal = user.getDefaultContext().getNameDe();
            } else {
                retVal = user.getDefaultContext().getNameEn();
            }
        } else {
            if(!result.hasErrors()){
                if (userSession.getContextId() > 0) {
                    Context found = contextService.findByIdAndUserAccount(userSession.getContextId(), user);
                    if(found != null){
                        if(locale.getLanguage().equalsIgnoreCase("de")){
                            retVal = found.getNameDe();
                        } else {
                            retVal = found.getNameEn();
                        }
                    }
                }
            }
        }
        return retVal;
    }

    @ModelAttribute("locale")
    public final String getCurrentLocale(Locale locale, Model model){
        return locale.getLanguage().toLowerCase();
    }

}
