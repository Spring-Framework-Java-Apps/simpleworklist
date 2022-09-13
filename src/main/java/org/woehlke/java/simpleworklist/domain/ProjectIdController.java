package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.meso.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Controller
@RequestMapping(path = "/project/{projectId}")
public class ProjectIdController extends AbstractController {

  private final ProjectControllerService projectControllerService;
  private final TaskMoveService taskMoveService;
  private final TaskService taskService;
  private final ContextService contextService;
  private final BreadcrumbService breadcrumbService;

  @Autowired
  public ProjectIdController(ProjectControllerService projectControllerService, TaskMoveService taskMoveService, TaskService taskService, ContextService contextService, BreadcrumbService breadcrumbService) {
    this.projectControllerService = projectControllerService;
    this.taskMoveService = taskMoveService;
    this.taskService = taskService;
    this.contextService = contextService;
    this.breadcrumbService = breadcrumbService;
  }

  @RequestMapping(path = "", method = RequestMethod.GET)
  public final String project(
    @PathVariable long projectId,
    @PageableDefault(sort = "orderIdProject", direction = Sort.Direction.DESC) Pageable pageable,
    @RequestParam(required = false) String message,
    @RequestParam(required = false) boolean isDeleted,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("/project/" + projectId);
    Context context = super.getContext(userSession);
    userSession.setLastProjectId(projectId);
    model.addAttribute("userSession", userSession);
    Project thisProject = null;
    Page<Task> taskPage = null;
    if (projectId != 0) {
      thisProject = projectControllerService.findByProjectId(projectId);
      taskPage = projectControllerService.findByProject(thisProject, pageable);
    } else {
      thisProject = new Project();
      thisProject.setId(0L);
      thisProject.setContext(context);
      taskPage = taskService.findByProjectRoot(context, pageable);
    }
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("thisProject", thisProject);
    model.addAttribute("taskPage", taskPage);
    if (message != null) {
      model.addAttribute("message", message);
      model.addAttribute("isDeleted", isDeleted);
    }
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return "project/id/show";
  }


  @RequestMapping(path = "/project/add", method = RequestMethod.GET)
  public final String projectAddProjectGet(
    @PathVariable long projectId,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    log.info("private addNewProjectGet (GET) projectId=" + projectId);
    Context context = super.getContext(userSession);
    projectControllerService.addNewProjectToProjectIdForm(projectId, userSession, context, locale, model);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return "project/id/project/add";
  }

  @RequestMapping(path = "/project/add", method = {RequestMethod.POST})
  public final String projectAddProjectPost(
    @PathVariable long projectId,
    @ModelAttribute("userSession") UserSessionBean userSession,
    @Valid Project project,
    BindingResult result,
    Locale locale, Model model
  ) {
    log.info("private addNewProjectPost (POST) projectId=" + projectId + " " + project.toString());
    Context context = super.getContext(userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    return projectControllerService.addNewProjectToProjectIdPersist(
      projectId,
      userSession,
      project,
      context,
      result,
      locale,
      model
    );
  }

  @RequestMapping(path = "/edit", method = RequestMethod.GET)
  public final String projectEditGet(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    Context context = super.getContext(userSession);
    UserAccount userAccount = context.getUserAccount();
    userSession.setLastProjectId(thisProject.getId());
    model.addAttribute("userSession", userSession);
    List<Context> contexts = contextService.getAllForUser(userAccount);
    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
    model.addAttribute("contexts", contexts);
    model.addAttribute("breadcrumb", breadcrumb);
    model.addAttribute("thisProject", thisProject);
    model.addAttribute("project", thisProject);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return "project/id/edit";
  }

  @RequestMapping(path = "/edit", method = RequestMethod.POST)
  public final String projectEditPost(
    @PathVariable long projectId,
    @Valid Project project,
    BindingResult result,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale, Model model
  ) {
    Context context = super.getContext(userSession);
    UserAccount thisUser = context.getUserAccount();
    Project thisProject;
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    if (result.hasErrors()) {
      for (ObjectError e : result.getAllErrors()) {
        log.info(e.toString());
      }
      thisProject = projectControllerService.findByProjectId(projectId);
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject, locale, userSession);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("userSession", userSession);
      return "project/id/edit";
    } else {
      thisProject = projectControllerService.findByProjectId(project.getId());
      thisProject.setName(project.getName());
      thisProject.setDescription(project.getDescription());
      Context newContext = project.getContext();
      boolean contextChanged = (newContext.getId().longValue() != thisProject.getContext().getId().longValue());
      if (contextChanged) {
        long newContextId = newContext.getId();
        newContext = contextService.findByIdAndUserAccount(newContextId, thisUser);
        thisProject = projectControllerService.moveProjectToAnotherContext(thisProject, newContext);
        userSession.setLastContextId(newContextId);
      } else {
        thisProject = projectControllerService.update(thisProject);
      }
      userSession.setLastProjectId(thisProject.getId());
      model.addAttribute("userSession", userSession);
      return thisProject.getUrl();
    }
  }

  @RequestMapping(path = "/delete", method = RequestMethod.GET)
  public final String projectDeleteGet(
    @PathVariable("projectId") Project project,
    @PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable request,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Locale locale,
    Model model
  ) {
    userSession.setLastProjectId(project.getId());
    model.addAttribute("userSession", userSession);
    boolean hasNoData = taskService.projectHasNoTasks(project);
    boolean hasNoChildren = project.hasNoChildren();
    boolean delete = hasNoData && hasNoChildren;
    if (delete) {
      Project parent = projectControllerService.delete(project);
      //TODO: message to message_properties
      String message = "Project is deleted. You see its parent project now.";
      //TODO: message to UserSessionBean userSession
      model.addAttribute("message", message);
      //TODO: isDeleted as message to UserSessionBean userSession
      model.addAttribute("isDeleted", true);
      model.addAttribute("userSession", userSession);
      if (parent == null) {
        return "redirect:/project/root";
      } else {
        return parent.getUrl();
      }
    } else {
      //TODO: message to message_properties
      StringBuilder s = new StringBuilder("Deletion rejected for this Project, because ");
      log.info("Deletion rejected for Project " + project.getId());
      if (!hasNoData) {
        //TODO: message to message_properties
        log.warn("Project " + project.getId() + " has actionItem");
        s.append("Project has actionItems.");
      }
      if (!hasNoChildren) {
        //TODO: message to message_properties
        log.info("Project " + project.getId() + " has children");
        s.append("Project has child categories.");
      }
      Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(project, locale, userSession);
      Page<Task> taskPage = taskService.findByProjectId(project, request);
      model.addAttribute("message", s.toString());
      model.addAttribute("isDeleted", false);
      model.addAttribute("taskPage", taskPage);
      model.addAttribute("breadcrumb", breadcrumb);
      model.addAttribute("thisProject", project);
      model.addAttribute("userSession", userSession);
      model.addAttribute("taskstateType", PROJECTS.getSlug());
      model.addAttribute("dataPage", true);
      return "project/id/show";
    }
  }

  @RequestMapping(path = "/project/move/to/project/{targetProjectId}", method = RequestMethod.GET)
  public final String projectMoveToProjectGet(
    @PathVariable("projectId") Project thisProject,
    @PathVariable long targetProjectId,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    model.addAttribute("userSession", userSession);
    Project targetProject = projectControllerService.findByProjectId(targetProjectId);
    thisProject = projectControllerService.moveProjectToAnotherProject(thisProject, targetProject);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
  public String moveTaskToTaskAndChangeTaskOrderInProject(
    @PathVariable("projectId") Project thisProject,
    @PathVariable("taskId") Task sourceTask,
    @PathVariable("destinationTaskId") Task destinationTask,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    model.addAttribute("userSession", userSession);
    log.info("-------------------------------------------------");
    log.info("  projectTaskChangeOrderToTaskGet");
    log.info("-------------------------------------------------");
    log.info("  source Task:      " + sourceTask.toString());
    log.info("-------------------------------------------------");
    log.info("  destination Task: " + destinationTask.toString());
    log.info("-------------------------------------------------");
    projectControllerService.moveTaskToTaskAndChangeTaskOrderInProjectId(sourceTask, destinationTask);
    log.info("  DONE: taskMoveService.moveOrderIdProject");
    log.info("-------------------------------------------------");
    model.addAttribute("userSession", userSession);
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/completed/move/to/trash", method = RequestMethod.GET)
  public final String moveAllCompletedToTrash(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    Context context = super.getContext(userSession);
    taskMoveService.moveAllCompletedToTrash(context);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }

  @RequestMapping(path = "/task/trash/empty", method = RequestMethod.GET)
  public final String emptyTrash(
    @PathVariable("projectId") Project thisProject,
    @ModelAttribute("userSession") UserSessionBean userSession,
    Model model
  ) {
    userSession.setLastProjectId(thisProject.getId());
    Context context = super.getContext(userSession);
    taskMoveService.emptyTrash(context);
    model.addAttribute("userSession", userSession);
    model.addAttribute("taskstateType", PROJECTS.getSlug());
    model.addAttribute("dataPage", true);
    return thisProject.getUrl();
  }
}
