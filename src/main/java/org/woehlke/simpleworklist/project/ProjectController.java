package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.task.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Controller
@RequestMapping(path = "/project")
public class ProjectController extends AbstractController {

    private final TaskService taskService;
    private final TaskMoveService taskMoveService;
    private final ProjectService projectService;

    private static final long rootProjectId = 0L;

    @Autowired
    public ProjectController(TaskService taskService, TaskMoveService taskMoveService, ProjectService projectService) {
        this.taskService = taskService;
        this.taskMoveService = taskMoveService;
        this.projectService = projectService;
    }

    @RequestMapping(path = "/root", method = RequestMethod.GET)
    public final String showRootProject(
            @PageableDefault(sort = "orderIdProject") Pageable pageable,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model) {
        log.info("/project/root");
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(0L);
        model.addAttribute("userSession",userSession);
        Page<Task> taskPage = taskService.findByRootProject(context,pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
            model.addAttribute("myTaskState","PROJECT");
        }
        return "project/root";
    }

    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
    public final String showProject(
            @PathVariable long projectId,
            @PageableDefault(sort = "orderIdProject") Pageable pageable,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model) {
        log.info("/project/"+projectId);
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = null;
        Page<Task> taskPage = null;
        if (projectId != 0) {
            thisProject = projectService.findByProjectId(projectId);
            taskPage = taskService.findByProject(thisProject, context, pageable);
        } else {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setContext(context);
            taskPage = taskService.findByRootProject(context, pageable);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
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

    @RequestMapping(path = "/add/new/project", method = RequestMethod.GET)
    public final String addNewTopLevelProjectForm(
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ){
        log.info("/project/add/new/project (GET)");
        return addNewProject(rootProjectId, userSession, locale, model);
    }


    @RequestMapping(path = "/add/new/project", method = RequestMethod.POST)
    public final String addNewTopLevelProjectSave(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ){
        log.info("/project/add/new/project (POST)");
        return addNewProjectPersist( rootProjectId, userSession, project, result, locale, model );
    }

    @RequestMapping(path = "/{thisProjectId}/move/to/{targetProjectId}", method = RequestMethod.GET)
    public final String moveProject(
            @PathVariable("thisProjectId") Project thisProject,
            @PathVariable long targetProjectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(thisProject.getId());
        model.addAttribute("userSession",userSession);
        Project targetProject = projectService.findByProjectId(targetProjectId);
        projectService.moveProjectToAnotherProject(thisProject, targetProject );
        return "redirect:/project/" + thisProject.getId();
    }

    @RequestMapping(path = "/{projectId}/edit", method = RequestMethod.GET)
    public final String editProjectGet(
            @PathVariable("projectId") Project thisProject,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(thisProject.getId());
        model.addAttribute("userSession",userSession);
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("areas", contexts);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", thisProject);
        return "project/edit";
    }

    @RequestMapping(path = "/{projectId}/edit", method = RequestMethod.POST)
    public final String editProjectPost(
            @PathVariable long projectId,
            @Valid Project project,
            BindingResult result,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Project thisProject = projectService.findByProjectId(projectId);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            return "project/edit";
        } else {
            Project thisProject = projectService.findByProjectId(project.getId());
            thisProject.setName(project.getName());
            thisProject.setDescription(project.getDescription());
            Context newContext = project.getContext();
            boolean areaChanged = (newContext.getId().longValue() != thisProject.getContext().getId().longValue());
            if(areaChanged){
                newContext = contextService.findByIdAndUserAccount(newContext.getId().longValue(), thisUser);
                projectService.moveProjectToAnotherContext(thisProject, newContext);
                model.addAttribute("userSession", new UserSessionBean(newContext.getId().longValue()));
            } else {
                projectService.saveAndFlush(thisProject);
            }
            return "redirect:/project/" + projectId;
        }
    }

    @RequestMapping(path = "/{projectId}/delete", method = RequestMethod.GET)
    public final String deleteProject(
            @PathVariable("projectId") Project project,
            @PageableDefault(sort = "title") Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model) {
        long newProjectId = project.getId();
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(project.getId());
        model.addAttribute("userSession",userSession);
        UserAccount userAccount = context.getUserAccount();
            if(project != null){
                boolean hasNoData = taskService.projectHasNoTasks(project);
                boolean hasNoChildren = project.hasNoChildren();
                if (hasNoData && hasNoChildren) {
                    if (!project.isRootProject()) {
                        newProjectId = project.getParent().getId();
                    } else {
                        newProjectId = 0;
                    }
                    projectService.delete(project);
                    String message = "Project is deleted. You see its parent project now.";
                    model.addAttribute("message",message);
                    model.addAttribute("isDeleted",true);
                } else {
                    StringBuilder s = new StringBuilder("Deletion rejected for this Project, because ");
                    log.info("Deletion rejected for Project " + project.getId());
                    if (!hasNoData) {
                        log.warn("Project " + project.getId() + " has actionItem");
                        s.append("Project has actionItems.");
                    }
                    if (!hasNoChildren) {
                        log.info("Project " + project.getId() + " has children");
                        s.append("Project has child categories.");
                    }
                    model.addAttribute("message",s.toString());
                    model.addAttribute("isDeleted",false);
                    Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(project,locale);
                    Page<Task> taskPage = taskService.findByProject(project, context, request);
                    model.addAttribute("taskPage", taskPage);
                    model.addAttribute("breadcrumb", breadcrumb);
                    model.addAttribute("thisProject", project);
                    return "project/show";
                }
            }
        return "redirect:/project/" + newProjectId;
    }


    private final String addNewProject(
        long projectId,
        UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        log.info("private addNewProject projectId="+projectId);
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = null;
        Project project = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            project = Project.newRootProjectFactory(userAccount);
            if(userSession.getContextId() == 0L){
                model.addAttribute("mustChooseArea", true);
                project.setContext(userAccount.getDefaultContext());
            } else {
                project.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId);
            project = Project.newProjectFactory(thisProject);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
        return "project/add";
    }

    private String addNewProjectPersist(
        long projectId,
        UserSessionBean userSession,
        Project project,
        BindingResult result,
        Locale locale, Model model
    ){
        log.info("private addNewProjectPersist projectId="+projectId+" "+project.toString());
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        if(result.hasErrors()){
            Project thisProject = null;
            if (projectId == 0) {
                thisProject = new Project();
                thisProject.setId(0L);
            } else {
                thisProject = projectService.findByProjectId(projectId);
            }
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return "project/add";
        } else {
            if (projectId == 0) {
                if(userSession.getContextId()>0) {
                    project.setContext(context);
                }
                project = projectService.saveAndFlush(project);
                projectId = project.getId();
            } else {
                Project thisProject = projectService.findByProjectId(projectId);
                List<Project> children = thisProject.getChildren();
                children.add(project);
                thisProject.setChildren(children);
                project.setParent(thisProject);
                project = projectService.saveAndFlush(project);
                projectId = project.getId();
                log.info("project:     "+ project.toString());
                log.info("thisProject: "+ thisProject.toString());
            }
            return "redirect:/project/" + projectId;
        }
    }

    @RequestMapping(path = "/{projectId}/add/new/project", method = RequestMethod.GET)
    public final String addNewProjectGet(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        log.info("private addNewProjectGet (GET) projectId="+projectId);
        return addNewProject(projectId, userSession, locale, model);
    }

    @RequestMapping(path = "/{projectId}/add/new/project",
            method = RequestMethod.POST)
    public final String addNewProjectPost(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            @Valid Project project,
            BindingResult result,
            Locale locale, Model model) {
        log.info("private addNewProjectPost (POST) projectId="+projectId+" "+project.toString());
        return addNewProjectPersist( projectId, userSession, project, result, locale, model );
    }

    @RequestMapping(path = "/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderIdWithinAProject(
            @PathVariable("sourceTaskId") Task sourceTask,
            @PathVariable("destinationTaskId") Task destinationTask,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ){
        Context context = super.getContext(userSession);
        if(!sourceTask.isInRootProject()){
            userSession.setLastProjectId(sourceTask.getProject().getId());
        }
        model.addAttribute("userSession",userSession);
        log.info("-------------------------------------------------");
        log.info("  changeTaskOrderIdWithinAProject");
        log.info("-------------------------------------------------");
        log.info("  source Task:      "+sourceTask.toString());
        log.info("-------------------------------------------------");
        log.info("  destination Task: "+destinationTask.toString());
        log.info("-------------------------------------------------");
        String returnUrl = "redirect:/taskstate/inbox";
        boolean rootProject = sourceTask.isInRootProject();
        returnUrl = "redirect:/project/0";
        if(rootProject){
            taskMoveService.moveOrderIdRootProject(sourceTask, destinationTask);
        } else {
            taskMoveService.moveOrderIdProject(sourceTask, destinationTask);
            log.info("  DONE: taskMoveService.moveOrderIdProject (2)");
            returnUrl = "redirect:/project/" + sourceTask.getProject().getId();
        }
        log.info("-------------------------------------------------");
        return returnUrl;
    }
}
