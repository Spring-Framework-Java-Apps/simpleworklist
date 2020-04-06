package org.woehlke.simpleworklist.project;

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
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskEnergy;
import org.woehlke.simpleworklist.task.TaskTime;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.user.account.UserAccount;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.user.UserSessionBean;

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

    private final ProjectControllerService projectControllerService;
    private final TaskMoveService taskMoveService;

    @Autowired
    public ProjectController(ProjectControllerService projectControllerService, TaskMoveService taskMoveService) {
        this.projectControllerService = projectControllerService;
        this.taskMoveService = taskMoveService;
    }

    @RequestMapping(path="/root", method = RequestMethod.GET)
    public final String showRootProject(
        @PageableDefault(sort = "orderIdProject", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) boolean isDeleted,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
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
        return "project/root/show";
    }

    @RequestMapping(path = "/root/add/project", method = RequestMethod.GET)
    public final String addNewTopLevelProjectForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ){
        log.info("/project/root/add/project (GET)");
        Context context = super.getContext(userSession);
        projectControllerService.addNewProjectToRoot(userSession, context, locale, model);
        return "project/root/add/project";
    }

    @RequestMapping(path = "/root/add/project", method = RequestMethod.POST)
    public final String addNewTopLevelProjectSave(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ){
        log.info("/project/root/add/project (POST)");
        Context context = super.getContext(userSession);
        return projectControllerService.addNewProjectToRootPersist(
            userSession,
            project,
            context,
            result,
            locale,
            model,
            "redirect:/project/"
        );
    }

    @RequestMapping(path = "/root/add/task", method = RequestMethod.GET)
    public final String addNewTaskToRootProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("/project/root/add/task (GET)");
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Project thisProject;
        Boolean mustChooseArea = false;
        thisProject = new Project();
        thisProject.setId(0L);
        if(userSession.getContextId() == 0L){
            mustChooseArea = true;
            task.setContext(userAccount.getDefaultContext());
            thisProject.setContext(userAccount.getDefaultContext());
        } else {
            task.setContext(context);
            thisProject.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("thisProjectId", thisProject.getId());
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "project/root/add/task";
    }

    @RequestMapping(path = "/root/add/task", method = RequestMethod.POST)
    public final String addNewTaskToRootProjectPost(
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result, Locale locale, Model model
    ) {
        log.info("/project/root/add/task (POST)");
        Context context = super.getContext(userSession);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Boolean mustChooseArea = false;
            task.setContext(context);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "project/root/add/task";
        } else {
            task.setProject(null);
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task.setFocus(false);
            task.setContext(context);
            long maxOrderIdProject = taskMoveService.getMaxOrderIdProject(task.getProject(),context);
            task.setOrderIdProject(++maxOrderIdProject);
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.saveAndFlush(task);
            log.info(task.toString());
            return "redirect:/project/root";
        }
    }

    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
    public final String showProject(
            @PathVariable long projectId,
            @PageableDefault(sort = "orderIdProject", direction = Sort.Direction.DESC) Pageable pageable,
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
        return "project/id/show";
    }

    @RequestMapping(path = "/{projectId}/add/project", method = RequestMethod.GET)
    public final String addNewSubProjectGet(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("private addNewProjectGet (GET) projectId="+projectId);
        Context context = super.getContext(userSession);
        projectControllerService.addNewProject(projectId, userSession, context, locale, model);
        return "project/id/add/project";
    }

    @RequestMapping(path = "/{projectId}/add/project", method = {RequestMethod.POST, RequestMethod.PUT})
    public final String addNewSubProjectPost(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Project project,
        BindingResult result,
        Locale locale, Model model) {
        log.info("private addNewProjectPost (POST) projectId="+projectId+" "+project.toString());
        Context context = super.getContext(userSession);
        return projectControllerService.addNewProjectPersist(
            projectId,
            userSession,
            project,
            context,
            result,
            locale,
            model ,
            "/add/project"
        );
    }


    @RequestMapping(path = "/{thisProjectId}/move/to/project/{targetProjectId}", method = RequestMethod.GET)
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
        return "project/id/edit";
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
            return "project/id/edit";
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
            @PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale,
            Model model
    ) {
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
                return "project/id/show";
            }
        }
        if( newProjectId == 0){
            return "redirect:/project/root/tasks";
        } else {
            return "redirect:/project/" + newProjectId;
        }
    }

    @RequestMapping(path = "/{projectId}/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderIdWithinAProject(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("sourceTaskId") Task sourceTask,
        @PathVariable("destinationTaskId") Task destinationTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
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

    @RequestMapping(path = "/{projectId}/add/task", method = RequestMethod.GET)
    public final String addNewTaskToProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Boolean mustChooseArea = false;
        if(userSession.getContextId() == 0L){
            mustChooseArea = true;
            task.setContext(userAccount.getDefaultContext());
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
            task.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProjectId", 0L);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "id/task/add";
    }

    @RequestMapping(path = "/{projectId}/add/task", method = RequestMethod.POST)
    public final String addNewTaskToProjectPost(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result, Locale locale, Model model) {
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Project thisProject = projectControllerService.getProject(projectId, userAccount, userSession);
            Boolean mustChooseArea = false;
            task.setProject(thisProject);
            task.setContext(thisProject.getContext());
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            //return "task/addToProject";
            return "id/task/add";
        } else {
            Project thisProject = projectService.findByProjectId(projectId);
            task.setProject(thisProject);
            task.setContext(thisProject.getContext());
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task.setFocus(false);
            task.setContext(context);
            long maxOrderIdProject = taskMoveService.getMaxOrderIdProject(task.getProject(),context);
            task.setOrderIdProject(++maxOrderIdProject);
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.saveAndFlush(task);
            log.info(task.toString());
            return "redirect:/project/" + projectId + "/";
        }
    }

}
