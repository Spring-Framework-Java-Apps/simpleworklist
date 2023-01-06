package org.woehlke.java.simpleworklist.domain;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Log
@Controller
@RequestMapping(path = "/project/root/task")
public class ProjectRootTaskMoveController extends AbstractController {

  public final static String rootProjectUrl = "redirect:/project/root";

  private final TaskLifecycleService taskLifecycleService;
  private final TaskMoveService taskMoveService;

  @Autowired
  public ProjectRootTaskMoveController(TaskLifecycleService taskLifecycleService, TaskMoveService taskMoveService) {
    this.taskLifecycleService = taskLifecycleService;
    this.taskMoveService = taskMoveService;
  }

  @RequestMapping(path = "/{taskId}/move/to/project/root", method = RequestMethod.GET)
  public final String moveTaskToProjectRoot(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = taskMoveService.moveTaskToRootProject(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
  public final String moveTaskToProjectId(
    @PathVariable("taskId") Task task,
    @PathVariable("projectId") Project targetProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = taskMoveService.moveTaskToAnotherProject(task,targetProject);
    userSession.setLastProjectId(targetProject.getId());
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return targetProject.getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
  public final String moveTaskToInbox(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to inbox");
    task.moveToInbox();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
  public final String moveTaskToToday(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to today");
    task.moveToToday();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
  public final String moveTaskToNext(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to next");
    task.moveToNext();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
  public final String moveTaskToWaiting(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to waiting");
    task.moveToWaiting();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
  public final String moveTaskToSomeday(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to someday");
    task.moveToSomeday();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
  public final String moveTaskToFocus(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to focus");
    task.moveToFocus();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
  public final String moveTaskToCompleted(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to completed");
    task.moveToCompletedTasks();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/{taskId}/move/to/trash", method = RequestMethod.GET)
  public final String moveTaskToTrash(
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to trash");
    task.moveToTrash();
    taskLifecycleService.updatedViaProjectRoot(task);
    userSession.setLastProjectId(Project.rootProjectId);
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/completed/move/to/trash", method = RequestMethod.GET)
  public final String moveAllCompletedToTrash(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    Context context = super.getContext(userSession);
    taskMoveService.moveAllCompletedToTrash(context);
    userSession.setLastContextId(context.getId());
    userSession.setLastProjectId(Project.rootProjectId);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

  @RequestMapping(path = "/trash/empty", method = RequestMethod.GET)
  public final String emptyTrash(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    Context context = super.getContext(userSession);
    taskMoveService.emptyTrash(context);
    userSession.setLastContextId(context.getId());
    userSession.setLastProjectId(Project.rootProjectId);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType",PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return rootProjectUrl;
  }

}
