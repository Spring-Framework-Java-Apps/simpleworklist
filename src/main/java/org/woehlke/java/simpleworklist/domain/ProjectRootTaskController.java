package org.woehlke.java.simpleworklist.domain;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Slf4j
@Controller
@RequestMapping(path = "/project/root/task")
public class ProjectRootTaskController extends AbstractController {

  public final static String rootProjectUrl = "redirect:/project/root";

  private final ProjectControllerService projectControllerService;
  private final TaskLifecycleService taskLifecycleService;
  private final TaskMoveService taskMoveService;
  private final TaskService taskService;
  private final BreadcrumbService breadcrumbService;

  @Autowired
  public ProjectRootTaskController(ProjectControllerService projectControllerService, TaskLifecycleService taskLifecycleService, TaskMoveService taskMoveService, TaskService taskService, BreadcrumbService breadcrumbService) {
    this.projectControllerService = projectControllerService;
    this.taskLifecycleService = taskLifecycleService;
    this.taskMoveService = taskMoveService;
    this.taskService = taskService;
    this.breadcrumbService = breadcrumbService;
  }

  @RequestMapping(path = "/add", method = RequestMethod.GET)
  public final String projectRootTaskAddGet(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("/project/root/add/task (GET)");
    Context context = super.getContext(userSession);
    UserAccount userAccount = context.getUserAccount();
    Task thisTask = new Task();
    thisTask.setTaskState(TaskState.INBOX);
    thisTask.setTaskEnergy(TaskEnergy.NONE);
    thisTask.setTaskTime(TaskTime.NONE);
    thisTask.unsetFocus();
    Project thisProject;
    Boolean mustChooseContext = false;
    thisProject = new Project();
    thisProject.setId(0L);
    if(userSession.getLastContextId() == 0L){
      mustChooseContext = true;
      thisTask.setContext(userAccount.getDefaultContext());
      thisProject.setContext(userAccount.getDefaultContext());
    } else {
      thisTask.setContext(context);
      thisProject.setContext(context);
    }
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject,locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("mustChooseContext", mustChooseContext); //TODO: rename mustChooseArea -> mustChooseContext
    model.addAttribute("thisProject", thisProject);
    model.addAttribute("thisProjectId", thisProject.getId());
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("task", thisTask);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return "project/root/task/add";
  }

  @RequestMapping(path = "/add", method = RequestMethod.POST)
  public final String projectRootTaskAddPost(
    @ModelAttribute("userSession") UserSessionBean userSession,
    @Valid Task task,
    BindingResult result,
    Locale locale,
    Model model
  ) {
    log.info("/project/root/task/add (POST)");
    Context context = super.getContext(userSession);
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", false);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    if (result.hasErrors()) {
      for (ObjectError e : result.getAllErrors()) {
        log.info(e.toString());
      }
      Boolean mustChooseArea = false;
      task.setContext(context);
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale,userSession);
      model.addAttribute("mustChooseArea", mustChooseArea);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("task", task);
      model.addAttribute("userSession", userSession);
      return "project/root/task/add";
    } else {
      task.setContext(context);
      task = taskLifecycleService.addToRootProject(task);
      log.info(task.toString());
      model.addAttribute("userSession", userSession);
      return rootProjectUrl;
    }
  }

  @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.GET)
  public final String editTaskGet(
    @PathVariable("taskId") Task thisTask,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("editTaskGet");
    List<Context> contexts = super.getContexts();
    Context thisContext = thisTask.getContext();
    Project thisProject = taskLifecycleService.addProjectFromTaskToModel( thisTask, model );
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(thisTask.getTaskState(),locale,userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("thisProject", thisProject); //TODO: remove?
    model.addAttribute("thisContext", thisContext);
    model.addAttribute("task", thisTask);
    model.addAttribute("contexts", contexts);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", true);
    return "project/root/task/edit";
  }

  @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.POST)
  public final String editTaskPost(
    @PathVariable long taskId,
    @Valid Task thisTask,
    @ModelAttribute("userSession") UserSessionBean userSession,
    BindingResult result,
    Locale locale,
    Model model
  ) {
    log.info("editTaskPost");
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    model.addAttribute("addProjectToTask", true);
    if(thisTask.getTaskState()==TaskState.SCHEDULED && thisTask.getDueDate()==null){
      String objectName="task";
      String field="dueDate";
      String defaultMessage="you need a due Date to schedule the Task";
      FieldError error = new FieldError(objectName,field,defaultMessage);
      result.addError(error);
      field="taskState";
      error = new FieldError(objectName,field,defaultMessage);
      result.addError(error);
    }
    if (result.hasErrors() ) {
      log.warn("result.hasErrors");
      for (ObjectError e : result.getAllErrors()) {
        log.warn(e.toString());
      }
      List<Context> contexts = super.getContexts();
      thisTask = taskLifecycleService.addProject(thisTask);
      Context thisContext = thisTask.getContext();
      Project thisProject = taskLifecycleService.addProjectFromTaskToModel( thisTask, model );
      //thisProject.setId(0L);
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("thisProject", thisProject); //TODO: remove?
      model.addAttribute("thisContext", thisContext);
      model.addAttribute("task", thisTask);
      model.addAttribute("contexts", contexts);
      userSession.setLastProjectId(thisProject.getId());
      userSession.setLastTaskState(thisTask.getTaskState());
      userSession.setLastTaskId(thisTask.getId());
      userSession.setLastContextId(thisContext.getId());
      model.addAttribute("userSession", userSession);
      return "project/root/task/edit";
    } else {
      //task.unsetFocus();
      thisTask.setLastProject(null);
      Task persistentTask  = taskLifecycleService.addProject(thisTask);
      thisTask = taskLifecycleService.updatedViaProjectRoot(persistentTask);
      userSession.setLastProjectId(Project.rootProjectId);
      userSession.setLastTaskState(thisTask.getTaskState());
      userSession.setLastTaskId(thisTask.getId());
      model.addAttribute("userSession", userSession);
      return thisTask.getTaskState().getUrlPathRedirect();
    }
  }

  @RequestMapping(path = "/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
  public String changeTaskOrderId(
    @PathVariable("taskId") Task sourceTask,
    @PathVariable("destinationTaskId") Task destinationTask,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ){
    model.addAttribute("userSession", userSession);
    log.info("------------- changeTaskOrderId -------------");
    log.info("source Task:      "+sourceTask.toString());
    log.info("---------------------------------------------");
    log.info("destination Task: "+destinationTask.toString());
    log.info("---------------------------------------------");
    projectControllerService.moveTaskToTaskAndChangeTaskOrderInProjectRoot(sourceTask, destinationTask);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(sourceTask.getTaskState());
    userSession.setLastTaskId(sourceTask.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/delete", method = RequestMethod.GET)
  public final String deleteTaskGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("deleteTaskGet");
    if(task!= null){
      task.delete();
      taskLifecycleService.updatedViaProjectRoot(task);
    }
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/undelete", method = RequestMethod.GET)
  public final String undeleteTaskGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("undeleteTaskGet");
    task.undelete();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/transform", method = RequestMethod.GET)
  public final String transformTaskIntoProjectGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("transformTaskIntoProjectGet");
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return taskLifecycleService.transformTaskIntoProjectGet(task, userSession, model);
  }

  @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
  public final String setDoneTaskGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task.complete();
    //long maxOrderIdTaskState = taskLifecycleService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
    //task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/incomplete", method = RequestMethod.GET)
  public final String unsetDoneTaskGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task.incomplete();
    //long maxOrderIdTaskState = taskLifecycleService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
    //task.setOrderIdTaskState(++maxOrderIdTaskState);
    task = taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/setfocus", method = RequestMethod.GET)
  public final String setFocusGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ){
    task.setFocus();
    task = taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/unsetfocus", method = RequestMethod.GET)
  public final String unsetFocusGet(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ){
    task.unsetFocus();
    task = taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

}
