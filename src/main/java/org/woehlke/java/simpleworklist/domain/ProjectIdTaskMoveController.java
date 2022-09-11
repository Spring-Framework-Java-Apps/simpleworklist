package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.AbstractController;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Slf4j
@Controller
@RequestMapping(path = "/project/{projectId}/task/{taskId}")
public class ProjectIdTaskMoveController extends AbstractController {

  private final TaskMoveService taskMoveService;
  private final TaskLifecycleService taskLifecycleService;

  public ProjectIdTaskMoveController(TaskMoveService taskMoveService, TaskLifecycleService taskLifecycleService) {
    this.taskMoveService = taskMoveService;
    this.taskLifecycleService = taskLifecycleService;
  }

  @RequestMapping(path = "/move/to/project/root", method = RequestMethod.GET)
  public final String moveTaskToAnotherProject(
    @PathVariable("projectId") Project thisProject,
    @PathVariable("taskId") Task task,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = taskMoveService.moveTaskToRootProject(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return "redirect:/project/root";
  }

  @RequestMapping(path = "/move/to/project/{otherProjectId}", method = RequestMethod.GET)
  public final String moveTaskToAnotherProject(
    @PathVariable("projectId") Project thisProject,
    @PathVariable("taskId") Task task,
    @PathVariable("otherProjectId") Project otherProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = taskMoveService.moveTaskToAnotherProject(task, otherProject);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return otherProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/inbox", method = RequestMethod.GET)
  public final String moveTaskToInbox(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to inbox");
    task.moveToInbox();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/today", method = RequestMethod.GET)
  public final String moveTaskToToday(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to today");
    task.moveToToday();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/next", method = RequestMethod.GET)
  public final String moveTaskToNext(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to next");
    task.moveToNext();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/waiting", method = RequestMethod.GET)
  public final String moveTaskToWaiting(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to waiting");
    task.moveToWaiting();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/someday", method = RequestMethod.GET)
  public final String moveTaskToSomeday(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to someday");
    task.moveToSomeday();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/focus", method = RequestMethod.GET)
  public final String moveTaskToFocus(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to focus");
    task.moveToFocus();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/taskstate/completed", method = RequestMethod.GET)
  public final String moveTaskToCompleted(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to completed");
    task.moveToCompletedTasks();
    taskLifecycleService.updatedViaTaskstate(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/move/to/trash", method = RequestMethod.GET)
  public final String moveTaskToTrash(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @PathVariable("taskId") Task task,
    Model model
  ) {
    log.info("dragged and dropped " + task.getId() + " to trash");
    task.moveToTrash();
    task = taskLifecycleService.updatedViaProject(task);
    userSession.setLastProjectId(thisProject.getId());
    userSession.setLastTaskState(task.getTaskState());
    userSession.setLastTaskId(task.getId());
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
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
}
