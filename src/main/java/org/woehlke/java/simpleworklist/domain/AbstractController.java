package org.woehlke.java.simpleworklist.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectService;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.db.user.chat.ChatMessageService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.security.login.LoginSuccessService;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.GERMAN;

/**
 * Created by tw on 14.02.16.
 */
@SessionAttributes({"userSession", "locale"})
public abstract class AbstractController {

  @Autowired
  private ContextService contextService;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ChatMessageService chatMessageService;

  @Autowired
  private LoginSuccessService loginSuccessService;


  @ModelAttribute("allProjects")
  public final List<Project> getAllProjects(
    @ModelAttribute("userSession") UserSessionBean userSession
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
  public final int getNumberOfNewIncomingMessages() {
    UserAccount user = this.getUser();
    return chatMessageService.getNumberOfNewIncomingMessagesForUser(user);
  }

  @ModelAttribute("contexts")
  public final List<Context> getContexts() {
    UserAccount user = this.getUser();
    return contextService.getAllForUser(user);
  }

  @ModelAttribute("listTaskEnergy")
  public final List<TaskEnergy> getListTaskEnergy() {
    return TaskEnergy.list();
  }

  @ModelAttribute("listTaskTime")
  public final List<TaskTime> getListTaskTime() {
    return TaskTime.list();
  }

  @ModelAttribute("listTaskState")
  public final List<TaskState> getTaskStates() {
    return TaskState.getLlist4Ui();
  }

  @ModelAttribute("context")
  public final String getCurrentContext(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale//,
    //Model model
  ) {
    userSession = updateUserSession(userSession);
    Context context = getContext(userSession);
    if (locale == null) {
      locale = Locale.ENGLISH;
    }
    if (locale == GERMAN) {
      return context.getNameDe();
    } else {
      return context.getNameEn();
    }
  }

  @ModelAttribute("refreshMessages")
  public final boolean refreshMessagePage() {
    return false;
  }

  protected UserAccount getUser() {
    return this.loginSuccessService.retrieveCurrentUser();
  }

  protected Context getContext(@NotNull final UserSessionBean userSession) {
    UserAccount thisUser = this.getUser();
    long defaultContextId = thisUser.getDefaultContext().getId();
    long userSessionLastContextId = userSession.getLastContextId();
    long newContextId = userSessionLastContextId;
    if (userSessionLastContextId == 0L) {
      newContextId = defaultContextId;
    }
    Context context = contextService.findByIdAndUserAccount(newContextId, thisUser);
    return context;
  }

  private UserSessionBean getNewUserSession() {
    UserAccount thisUser = this.getUser();
    long userAccountid = thisUser.getId();
    long contextId = thisUser.getDefaultContext().getId();
    UserSessionBean userSession = new UserSessionBean(userAccountid, contextId);
    return userSession;
  }

  private UserSessionBean updateUserSession(UserSessionBean userSession) {
    if (userSession == null) {
      userSession = getNewUserSession();
    } else {
      UserAccount thisUser = this.getUser();
      long contextId = thisUser.getDefaultContext().getId();
      long userAccountid = thisUser.getId();
      userSession.update(userAccountid, contextId);
    }
    return userSession;
  }

}
