package org.woehlke.simpleworklist.application.framework;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.simpleworklist.domain.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.TaskService;
import org.woehlke.simpleworklist.domain.taskworkflow.TaskState;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.simpleworklist.domain.task.TaskEnergy;
import org.woehlke.simpleworklist.domain.task.TaskTime;
import org.woehlke.simpleworklist.domain.context.ContextService;
import org.woehlke.simpleworklist.domain.project.ProjectService;
import org.woehlke.simpleworklist.domain.chat.ChatMessageService;
import org.woehlke.simpleworklist.domain.user.account.UserAccountService;
import org.woehlke.simpleworklist.application.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.domain.user.access.UserAccountAccessService;
import org.woehlke.simpleworklist.domain.user.login.UserAccountLoginSuccessService;

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
    protected SimpleworklistProperties simpleworklistProperties;

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected UserAccountAccessService userAccountAccessService;

    @Autowired
    protected ChatMessageService chatMessageService;

    @Autowired
    protected UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    protected ContextService contextService;

    @Autowired
    protected BreadcrumbService breadcrumbService;

    @ModelAttribute("allProjects")
    public final List<Project> getAllCategories(
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result, //TODO: remove
        Model model  //TODO: remove
    ) {
        userSession = updateUserSession(userSession);
        Context context = this.getContext(userSession);
        return projectService.findAllProjectsByContext(context);
    }

    @ModelAttribute("rootProjects")
    public final List<Project> getRootCategories(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        userSession = updateUserSession(userSession);
        Context context = this.getContext(userSession);
        return projectService.findRootProjectsByContext(context);
    }

    @ModelAttribute("numberOfNewIncomingMessages")
    public final int getNumberOfNewIncomingMessages(){
        UserAccount user = this.getUser();
        return chatMessageService.getNumberOfNewIncomingMessagesForUser(user);
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
        Locale locale,
        Model model
    ){
        userSession = updateUserSession(userSession);
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

    protected UserSessionBean getNewUserSession(){
        UserAccount thisUser = this.getUser();
        long userAccountid = thisUser.getId();
        long contextId = thisUser.getDefaultContext().getId();
        UserSessionBean userSession = new UserSessionBean(userAccountid,contextId);
        return userSession;
    }

    protected UserSessionBean updateUserSession(UserSessionBean userSession){
        if(userSession == null){
            userSession = getNewUserSession();
        } else {
            UserAccount thisUser = this.getUser();
            long contextId = thisUser.getDefaultContext().getId();
            long userAccountid = thisUser.getId();
            userSession.update(userAccountid,contextId);
        }
        return userSession;
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
