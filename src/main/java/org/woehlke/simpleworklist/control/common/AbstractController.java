package org.woehlke.simpleworklist.control.common;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.simpleworklist.config.ApplicationProperties;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.enumerations.TaskEnergy;
import org.woehlke.simpleworklist.oodm.enumerations.TaskTime;
import org.woehlke.simpleworklist.oodm.services.ContextService;
import org.woehlke.simpleworklist.oodm.services.ProjectService;
import org.woehlke.simpleworklist.oodm.services.User2UserMessageService;
import org.woehlke.simpleworklist.oodm.services.UserAccountService;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.model.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Locale;

/**
 * Created by tw on 14.02.16.
 */
@SessionAttributes({"userSession","locale"})
public abstract class AbstractController {

    @Autowired
    protected ApplicationProperties applicationProperties;

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected UserAccountAccessService userAccountAccessService;

    @Autowired
    protected User2UserMessageService user2UserMessageService;

    @Autowired
    protected UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    protected ContextService contextService;

    @Autowired
    protected BreadcrumbService breadcrumbService;

    @ModelAttribute("allCategories")
    public final List<Project> getAllCategories(@ModelAttribute("userSession") UserSessionBean userSession,
                                                BindingResult result, Model model) {
        Context context = this.getContext(userSession);
        return projectService.findAllProjectsByContext(context);
    }

    @ModelAttribute("rootCategories")
    public final List<Project> getRootCategories(
            @ModelAttribute("userSession") UserSessionBean userSession,
             BindingResult result, Model model
    ) {
        Context context = this.getContext(userSession);
        return projectService.findRootProjectsByContext(context);
    }

    @ModelAttribute("numberOfNewIncomingMessages")
    public final int getNumberOfNewIncomingMessages(){
        UserAccount user = this.getUser();
        return user2UserMessageService.getNumberOfNewIncomingMessagesForUser(user);
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
        UserAccount user = this.getUser();
        return contextService.getAllForUser(user);
    }

    @ModelAttribute("context")
    public final String getCurrentArea(@ModelAttribute("userSession") UserSessionBean userSession,
                                       Locale locale){
        Context context = this.getContext(userSession);
        if(locale == Locale.GERMAN){
            return context.getNameDe();
        } else {
            return context.getNameEn();
        }
    }

    @ModelAttribute("refreshMessages")
    public final boolean refreshMessagePage(){
        return false;
    }

    protected UserAccount getUser(){
        return this.userAccountLoginSuccessService.retrieveCurrentUser();
    }

    protected Context getContext(UserSessionBean userSession){
        UserAccount thisUser = this.getUser();
        long defaultContextId = thisUser.getDefaultContext().getId();
        if(userSession == null){
            userSession = new UserSessionBean(defaultContextId);
        }
        long contextId = userSession.getContextId();
        if(contextId == 0){
            userSession.setContextId(defaultContextId);
        }
        return contextService.findByIdAndUserAccount(contextId, thisUser);
    }

}
