package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.model.services.TaskMoveService;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.oodm.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Controller
@RequestMapping(value = "/project")
public class ProjectController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private final TaskService taskService;

    private final TaskMoveService taskMoveService;

    @Autowired
    public ProjectController(TaskService taskService, TaskMoveService taskMoveService) {
        this.taskService = taskService;
        this.taskMoveService = taskMoveService;
    }

    @RequestMapping(value = "/root", method = RequestMethod.GET)
    public final String showRootProject(
            @PageableDefault(sort = "orderIdProject") Pageable pageable,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);

        //Project thisProject = new Project();
        //thisProject.setId(0L);
        //thisProject.setUserAccount(userAccount);
        //thisProject.setContext(context);
        Page<Task> taskPage = taskService.findByRootProject(pageable, userAccount, context);

        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(userAccount);
        model.addAttribute("breadcrumb", breadcrumb);
        //model.addAttribute("thisProject", thisProject);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
            model.addAttribute("myTaskState","PROJECT");
        }
        return "project/root";
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public final String showProject(
            @PathVariable long projectId,
            @PageableDefault(sort = "orderIdProject") Pageable pageable,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        Project thisProject = null;
        Page<Task> taskPage = null;
        if (projectId != 0) {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            taskPage = taskService.findByProject(thisProject, pageable, userAccount, context);
        } else {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            thisProject.setContext(context);
            taskPage = taskService.findByRootProject(pageable, userAccount, context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("myTaskState","PROJECT");
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
        }
        return "project/show";
    }

    @RequestMapping(value = "/add/new/project", method = RequestMethod.GET)
    public final String addNewProjectForm(
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model
    ){
        return addNewProjectGet(0L, userSession,model);
    }

    @RequestMapping(value = "/{thisProjectId}/move/to/{targetProjectId}", method = RequestMethod.GET)
    public final String moveProject(
            @PathVariable("thisProjectId") Project thisProject,
            @PathVariable long targetProjectId,
            @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        //Project thisProject = null;
        //if (thisProjectId != 0) {
            //thisProject = projectService.findByProjectId(thisProjectId, userAccount);
            Project targetProject = projectService.findByProjectId(targetProjectId, userAccount);
            projectService.moveProjectToAnotherProject(thisProject, targetProject, userAccount );
        //}
        return "redirect:/project/" + thisProject.getId();
    }

    @RequestMapping(value = "/{projectId}/edit", method = RequestMethod.GET)
    public final String editProjectGet(
            @PathVariable("projectId") Project thisProject,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model
    ) {
        //if (projectId > 0) {
            UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
            List<Context> contexts = contextService.getAllForUser(userAccount);
            //Project thisProject = projectService.findByProjectId(projectId, userAccount);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("areas", contexts);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", thisProject);
            return "project/edit";
        //} else {
          //  return "redirect:/project/0/";
        //}
    }

    @RequestMapping(value = "/{projectId}/edit", method = RequestMethod.POST)
    public final String editProjectPost(
            @PathVariable long projectId,
            @Valid Project project,
            BindingResult result,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model
    ) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            Project thisProject = projectService.findByProjectId(projectId, userAccount);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            return "project/edit";
        } else {
            Project thisProject = projectService.findByProjectId(project.getId(), userAccount);
            thisProject.setName(project.getName());
            thisProject.setDescription(project.getDescription());
            Context newContext = project.getContext();
            boolean areaChanged = (newContext.getId().longValue() != thisProject.getContext().getId().longValue());
            if(areaChanged){
                newContext = contextService.findByIdAndUserAccount(newContext.getId().longValue(), userAccount);
                projectService.moveProjectToAnotherContext(thisProject, newContext, userAccount);
                model.addAttribute("userSession", new UserSessionBean(newContext.getId().longValue()));
            } else {
                projectService.saveAndFlush(thisProject, userAccount);
            }
            return "redirect:/project/" + projectId;
        }
    }

    @RequestMapping(value = "/{projectId}/delete", method = RequestMethod.GET)
    public final String deleteProject(
            @PathVariable("projectId") Project project,
            @PageableDefault(sort = "title") Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model) {
        long newProjectId = project.getId();
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        //if (projectId > 0) {
            //Project project = projectService.findByProjectId(projectId, userAccount);
            if(project != null){
                boolean hasNoData = taskService.projectHasNoTasks(project, userAccount);
                boolean hasNoChildren = project.hasNoChildren();
                if (hasNoData && hasNoChildren) {
                    if (!project.isRootProject()) {
                        newProjectId = project.getParent().getId();
                    } else {
                        newProjectId = 0;
                    }
                    projectService.delete(project, userAccount);
                    String message = "Project is deleted. You see its parent project now.";
                    model.addAttribute("message",message);
                    model.addAttribute("isDeleted",true);
                } else {
                    StringBuilder s = new StringBuilder("Deletion rejected for this Project, because ");
                    LOGGER.info("Deletion rejected for Project " + project.getId());
                    if (!hasNoData) {
                        LOGGER.warn("Project " + project.getId() + " has actionItem");
                        s.append("Project has actionItems.");
                    }
                    if (!hasNoChildren) {
                        LOGGER.info("Project " + project.getId() + " has children");
                        s.append("Project has child categories.");
                    }
                    model.addAttribute("message",s.toString());
                    model.addAttribute("isDeleted",false);
                    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(project, userAccount);
                    Page<Task> taskPage = taskService.findByProject(project, request, userAccount, context);
                    model.addAttribute("taskPage", taskPage);
                    model.addAttribute("breadcrumb", breadcrumb);
                    model.addAttribute("thisProject", project);
                    return "project/show";
                }
            }
        //}
        return "redirect:/project/" + newProjectId;
    }


    @RequestMapping(value = "/{projectId}/add/new/project", method = RequestMethod.GET)
    public final String addNewProjectGet(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model
    ) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Project thisProject = null;
        Project project = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            project = Project.newRootProjectFactory(userAccount);
            if(userSession.getContextId() == 0L){
                model.addAttribute("mustChooseArea", true);
                project.setContext(userAccount.getDefaultContext());
            } else {
                Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                project.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            project = Project.newProjectFactory(thisProject);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
        return "project/add";
    }

    @RequestMapping(value = "/{projectId}/add/new/project",
            method = RequestMethod.POST)
    public final String addNewProjectPost(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            @Valid Project project,
            BindingResult result,
            Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        if(result.hasErrors()){
            Project thisProject = null;
            if (projectId == 0) {
                thisProject = new Project();
                thisProject.setId(0L);
                thisProject.setUserAccount(userAccount);
            } else {
                thisProject = projectService.findByProjectId(projectId, userAccount);
            }
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return "project/add";
        } else {
            project.setUserAccount(userAccount);
            if (projectId == 0) {
                if(userSession.getContextId()>0) {
                    Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                    project.setContext(context);
                }
                project = projectService.saveAndFlush(project, userAccount);
                projectId = project.getId();
            } else {
                Project thisProject = projectService.findByProjectId(projectId, userAccount);
                List<Project> children = thisProject.getChildren();
                children.add(project);
                thisProject.setChildren(children);
                project.setParent(thisProject);
                project = projectService.saveAndFlush(project, userAccount);
                projectId = project.getId();
                LOGGER.info("project:     "+ project.toString());
                LOGGER.info("thisProject: "+ thisProject.toString());
            }
            return "redirect:/project/" + projectId;
        }
    }

    @RequestMapping(value = "/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderIdWithinAProject(
            @PathVariable("sourceTaskId") Task sourceTask,
            @PathVariable("destinationTaskId") Task destinationTask,
            @ModelAttribute("userSession") UserSessionBean userSession){
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        //Task sourceTask = taskService.findOne(sourceTaskId,userAccount);
        //Task destinationTask = taskService.findOne(destinationTaskId,userAccount);
        LOGGER.info("-------------------------------------------------");
        LOGGER.info("  changeTaskOrderIdWithinAProject");
        LOGGER.info("-------------------------------------------------");
        LOGGER.info("  source Task:      "+sourceTask.toString());
        LOGGER.info("-------------------------------------------------");
        LOGGER.info("  destination Task: "+destinationTask.toString());
        LOGGER.info("-------------------------------------------------");
        String returnUrl = "redirect:/taskstate/inbox";
        Project project = sourceTask.getProject();
        if(sourceTask.getUserAccount().getId().longValue()==destinationTask.getUserAccount().getId().longValue()){
            if(sourceTask.getProject() == null && destinationTask.getProject() == null) {
                taskMoveService.moveOrderIdProject(project, sourceTask,destinationTask, context);
                returnUrl = "redirect:/project/0";
            } else if (sourceTask.getProject() != null && destinationTask.getProject() != null) {
                boolean sameProject = (sourceTask.getProject().getId().longValue() == destinationTask.getProject().getId().longValue());
                if (sameProject) {
                    taskMoveService.moveOrderIdProject(project, sourceTask,destinationTask, context);
                    returnUrl = "redirect:/project/" + sourceTask.getProject().getId();
                }
            }
        }
        return returnUrl;
    }
}
