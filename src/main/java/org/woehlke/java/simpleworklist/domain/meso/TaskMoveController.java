package org.woehlke.java.simpleworklist.domain.meso;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.domain.AbstractController;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.taskworkflow.MoveTaskService;

import javax.validation.constraints.NotNull;

@Slf4j
@Controller
@RequestMapping(path = "/taskstate/task")
public class TaskMoveController extends AbstractController {

  private final MoveTaskService moveTaskService;

  @Autowired
  public TaskMoveController(MoveTaskService moveTaskService) {
    this.moveTaskService = moveTaskService;
  }

  @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
  public final String moveTaskToAnotherProject(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @PathVariable("projectId") Project project,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = moveTaskService.moveTaskToAnotherProject(task,project);
    userSession.setLastProjectId(project.getId());
    model.addAttribute("userSession",userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/project/root", method = RequestMethod.GET)
  public final String moveTaskToRootProject(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    task = moveTaskService.moveTaskToRootProject(task);
    userSession.setLastProjectId(0L);
    model.addAttribute("userSession",userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
  public final String moveTaskToInbox(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to inbox");
    task = moveTaskService.moveTaskToInbox(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
  public final String moveTaskToToday(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to today");
    task = moveTaskService.moveTaskToToday(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
  public final String moveTaskToNext(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to next");
    task = moveTaskService.moveTaskToNext(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
  public final String moveTaskToWaiting(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to waiting");
    task = moveTaskService.moveTaskToWaiting(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
  public final String moveTaskToSomeday(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to someday");
    task = moveTaskService.moveTaskToSomeday(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
  public final String moveTaskToFocus(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to focus");
    task = moveTaskService.moveTaskToFocus(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
  public final String moveTaskToCompleted(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to completed");
    task = moveTaskService.moveTaskToCompleted(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/{taskId}/move/to/taskstate/trash", method = RequestMethod.GET)
  public final String moveTaskToTrash(
    @NotNull @PathVariable("taskId") Task task,
    @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    log.info("dragged and dropped "+task.getId()+" to trash");
    task = moveTaskService.moveTaskToTrash(task);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return task.getTaskState().getUrl();
  }

  @RequestMapping(path = "/completed/move/to/trash", method = RequestMethod.GET)
  public final String moveAllCompletedToTrash(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    Context context = super.getContext(userSession);
    moveTaskService.moveAllCompletedToTrash(context);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return "redirect:/taskstate/trash";
  }

  @RequestMapping(path = "/trash/empty", method = RequestMethod.GET)
  public final String emptyTrash(
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    Context context = super.getContext(userSession);
    moveTaskService.emptyTrash(context);
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return "redirect:/taskstate/trash";
  }

}
