package org.woehlke.simpleworklist.common.domain;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.simpleworklist.domain.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.application.ApplicationProperties;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.TaskService;
import org.woehlke.simpleworklist.domain.task.TaskState;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.domain.task.TaskEnergy;
import org.woehlke.simpleworklist.domain.task.TaskTime;
import org.woehlke.simpleworklist.domain.context.ContextService;
import org.woehlke.simpleworklist.domain.project.ProjectService;
import org.woehlke.simpleworklist.user.services.User2UserMessageService;
import org.woehlke.simpleworklist.user.services.UserAccountService;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.services.UserAccountAccessService;
import org.woehlke.simpleworklist.user.services.UserAccountLoginSuccessService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.GERMAN;

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
    protected TaskService taskService;

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

    //TODO: rename allCategories to allProjects
    @ModelAttribute("allCategories")
    public final List<Project> getAllCategories(
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result, //TODO: remove
        Model model  //TODO: remove
    ) {
        Context context = this.getContext(userSession);
        return projectService.findAllProjectsByContext(context);
    }

    //TODO: rename rootCategories to rootProjects
    @ModelAttribute("rootCategories")
    public final List<Project> getRootCategories(
        @ModelAttribute("userSession") UserSessionBean userSession
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

    @ModelAttribute("listTaskState")
    public final List<TaskState> getTaskStates(){
        List<TaskState> listTaskState = new ArrayList<>(TaskState.values().length);
        for(TaskState taskState:TaskState.values()){
            listTaskState.add(taskState);
        }
        return listTaskState;
    }

    @ModelAttribute("context")
    public final String getCurrentContext(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale
    ){
        if(userSession == null){
            userSession = new UserSessionBean();
        }
        Context context = getContext(userSession);
        if(locale == null){
            locale = Locale.ENGLISH;
        }
        if(locale == GERMAN){
            return context.getNameDe();
        } else {
            return context.getNameEn();
        }
    }

    @ModelAttribute("refreshMessages")
    public final boolean refreshMessagePage(){
        return false;
    }

    protected UserAccount getUser() {
        return this.userAccountLoginSuccessService.retrieveCurrentUser();
    }

    protected Context getContext(@NotNull final UserSessionBean userSession){
        UserAccount thisUser = this.getUser();
        long defaultContextId = thisUser.getDefaultContext().getId();
        long userSessionLastContextId = userSession.getLastContextId();
        long newContextId = userSessionLastContextId;
        if(userSessionLastContextId == 0L){
            newContextId = defaultContextId;
        }
        Context context = contextService.findByIdAndUserAccount(newContextId, thisUser);
        return context;
    }

    /*
    protected Context getContext(UserSessionBean userSession){
        UserAccount thisUser = this.getUser();
        if(userSession == null){
            userSession = new UserSessionBean();
        }
        long defaultContextId = thisUser.getDefaultContext().getId();
        userSession.setLastContextId(defaultContextId);
        Context context = contextService.findByIdAndUserAccount(userSession.getLastContextId(), thisUser);
        userSession.setLastContextId(context.getId());
        userSession.setUserAccountid(thisUser.getId());
        return context;
    }
    */

}
