package org.woehlke.java.simpleworklist.application.framework;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.woehlke.java.simpleworklist.domain.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.config.SimpleworklistProperties;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.project.Project;
import org.woehlke.java.simpleworklist.domain.task.TaskService;
import org.woehlke.java.simpleworklist.domain.taskworkflow.TaskState;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.java.simpleworklist.domain.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.context.ContextService;
import org.woehlke.java.simpleworklist.domain.project.ProjectService;
import org.woehlke.java.simpleworklist.domain.chat.ChatMessageService;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.java.simpleworklist.domain.user.access.UserAuthorizationService;
import org.woehlke.java.simpleworklist.domain.user.login.UserAccountLoginSuccessService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.GERMAN;
import static org.woehlke.java.simpleworklist.domain.taskworkflow.TaskState.*;

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
    protected UserAuthorizationService userAuthorizationService;

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
      TaskState[] listTaskStateArray = {
        INBOX,
        TODAY,
        NEXT,
        WAITING,
        SCHEDULED,
        SOMEDAY,
        FOCUS,
        COMPLETED
      };
      List<TaskState> listTaskState = new ArrayList<>(listTaskStateArray.length);
      for(TaskState taskState:listTaskStateArray){
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
