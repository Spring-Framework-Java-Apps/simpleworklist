package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.meso.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Log
@Controller
@RequestMapping(path = "/project/{projectId}/task")
public class ProjectIdTaskController extends AbstractController {

  private final ProjectControllerService projectControllerService;
  private final ProjectService projectService;
  private final TaskLifecycleService taskLifecycleService;
  private final BreadcrumbService breadcrumbService;
  private final ContextService contextService;

  @Autowired
  public ProjectIdTaskController(
      ProjectControllerService projectControllerService,
      ProjectService projectService,
      TaskLifecycleService taskLifecycleService,
      BreadcrumbService breadcrumbService,
      ContextService contextService
  ) {
    this.projectControllerService = projectControllerService;
    this.projectService = projectService;
    this.taskLifecycleService = taskLifecycleService;
    this.breadcrumbService = breadcrumbService;
    this.contextService = contextService;
  }

  @RequestMapping(path = "/add", method = RequestMethod.GET)
  public final String projectTaskAddGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    UserAccount userAccount = super.getUser();
    Task task = new Task();
    task.setTaskState(TaskState.INBOX);
    task.setTaskEnergy(TaskEnergy.NONE);
    task.setTaskTime(TaskTime.NONE);
    task.setProject(thisProject);
    task.unsetFocus();
    Boolean mustChooseArea = false;
    if (userSession.getLastContextId() == 0L) {
      mustChooseArea = true;
      task.setContext(userAccount.getDefaultContext());
    } else {
      Context context = contextService.findByIdAndUserAccount(userSession.getLastContextId(), userAccount);
      task.setContext(context);
    }
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowProjectRoot(locale, userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("mustChooseArea", mustChooseArea);
    model.addAttribute("thisProject", thisProject);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("task", task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", false);
    return "project/id/task/add";
  }

  @RequestMapping(path = "/add", method = RequestMethod.POST)
  public final String projectTaskAddPost(
    @PathVariable long projectId,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @Valid Task task,
    BindingResult result,
    Locale locale, Model model
  ) {
    Context context = super.getContext(userSession);
    UserAccount userAccount = context.getUserAccount();
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", false);
    if (result.hasErrors()) {
      for (ObjectError e : result.getAllErrors()) {
        log.info(e.toString());
      }
      Project thisProject = projectControllerService.getProject(projectId, userAccount, userSession);
      Boolean mustChooseArea = false;
      task.setProject(thisProject);
      task.setContext(thisProject.getContext());
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
      model.addAttribute("mustChooseArea", mustChooseArea);
      model.addAttribute("thisProject", thisProject);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("task", task);
      model.addAttribute("userSession", userSession);
      return "project/id/task/add";
    } else {
      Project thisProject = projectService.findByProjectId(projectId);
      task.setProject(thisProject);
      task.setContext(thisProject.getContext());
      if (task.getDueDate() == null) {
        task.setTaskState(TaskState.INBOX);
      } else {
        task.setTaskState(TaskState.SCHEDULED);
      }
      task.setFocus(false);
      task.setContext(context);
      long maxOrderIdProject = taskLifecycleService.getMaxOrderIdProject(task.getProject(), context);
      task.setOrderIdProject(++maxOrderIdProject);
      long maxOrderIdTaskState = taskLifecycleService.getMaxOrderIdTaskState(task.getTaskState(), task.getContext());
      task.setOrderIdTaskState(++maxOrderIdTaskState);
      task = taskLifecycleService.addToProject(task);
      log.info(task.toString());
      model.addAttribute("userSession", userSession);
      return thisProject.getUrl();
    }
  }

  @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.GET)
  public final String editTaskGet(
    @PathVariable("projectId") Project thisProject,
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("editTaskGet");
    List<Context> contexts =  super.getContexts();
    Context thisContext = task.getContext();
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("thisProject", thisProject);
    model.addAttribute("thisContext", thisContext);
    model.addAttribute("task", task);
    model.addAttribute("contexts", contexts);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", true);
    return "project/id/task/edit";
  }

  @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.POST)
  public final String editTaskPost(
    @PathVariable("projectId") Project thisProject,
    @PathVariable long taskId,
    @Valid Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    BindingResult result,
    Locale locale,
    Model model
  ) {
    log.info("editTaskPost");
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", true);
    if (task.getTaskState() == TaskState.SCHEDULED && task.getDueDate() == null) {
      String objectName = "task";
      String field = "dueDate";
      String defaultMessage = "you need a due Date to schedule the Task";
      FieldError error = new FieldError(objectName, field, defaultMessage);
      result.addError(error);
      field = "taskState";
      error = new FieldError(objectName, field, defaultMessage);
      result.addError(error);
    }
    if (result.hasErrors()) {
      log.info("result.hasErrors");
      for (ObjectError e : result.getAllErrors()) {
        log.info(e.toString());
      }
      UserAccount userAccount = super.getUser();
      List<Context> contexts = super.getContexts();
      task = taskLifecycleService.addProject(task);
      Context thisContext = task.getContext();
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("thisProject", thisProject);
      model.addAttribute("thisContext", thisContext);
      model.addAttribute("task", task);
      model.addAttribute("contexts", contexts);
      userSession.setLastProjectId(thisProject.getId());
      userSession.setLastTaskState(task.getTaskState());
      userSession.setLastTaskId(task.getId());
      userSession.setLastContextId(thisContext.getId());
      model.addAttribute("userSession", userSession);
      return "project/id/task/edit";
    } else {
      task.setLastProject(thisProject);
      Task persistentTask = taskLifecycleService.addProject(task);
      persistentTask.merge(task);
      task = taskLifecycleService.updatedViaProject(persistentTask);
      userSession.setLastProjectId(Project.rootProjectId);
      userSession.setLastTaskState(task.getTaskState());
      userSession.setLastTaskId(task.getId());
      model.addAttribute("userSession", userSession);
      return thisProject.getUrl();
    }
  }

  @RequestMapping(path = "/transform", method = RequestMethod.GET)
  public final String transformTaskIntoProjectGet(
    @PathVariable("projectId") Project thisProject,
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("transformTaskIntoProjectGet");
    userSession.setLastProjectId(thisProject.getId());
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return taskLifecycleService.transformTaskIntoProjectGet(task, userSession, model);
  }

  @RequestMapping(path = "/task/{taskId}/complete", method = RequestMethod.GET)
  public final String setDoneTaskGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    task.complete();
    long maxOrderIdTaskState = taskLifecycleService.getMaxOrderIdTaskState(TaskState.COMPLETED, task.getContext());
    task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/{taskId}/incomplete", method = RequestMethod.GET)
  public final String unsetDoneTaskGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    task.incomplete();
    long maxOrderIdTaskState = taskLifecycleService.getMaxOrderIdTaskState(task.getTaskState(), task.getContext());
    task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/{taskId}/setfocus", method = RequestMethod.GET)
  public final String setFocusGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    @RequestParam(required = false) String back,
    Model model
  ) {
    task.setFocus();
    task = taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/{taskId}/unsetfocus", method = RequestMethod.GET)
  public final String unsetFocusGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    @RequestParam(required = false) String back,
    Model model
  ) {
    task.unsetFocus();
    task = taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

}
