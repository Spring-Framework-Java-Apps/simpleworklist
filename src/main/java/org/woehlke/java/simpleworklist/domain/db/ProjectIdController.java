package org.woehlke.java.simpleworklist.domain.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.java.simpleworklist.domain.AbstractController;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.meso.taskworkflow.TaskMoveService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.meso.taskworkflow.TransformTaskIntoProjektService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

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
    private final TransformTaskIntoProjektService transformTaskIntoProjektService;

    @Autowired
    public ProjectIdController(
      ProjectControllerService projectControllerService,
      TaskMoveService taskMoveService, TaskService taskService,
      TransformTaskIntoProjektService transformTaskIntoProjektService
    ) {
        this.projectControllerService = projectControllerService;
        this.taskMoveService = taskMoveService;
        this.taskService = taskService;
        this.transformTaskIntoProjektService = transformTaskIntoProjektService;
    }

    @RequestMapping(path = "/task/add", method = RequestMethod.GET)
    public final String projectTaskAddGet(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        task.setProject(thisProject);
        task.unsetFocus();
        Boolean mustChooseArea = false;
        if(userSession.getLastContextId() == 0L){
            mustChooseArea = true;
            task.setContext(userAccount.getDefaultContext());
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getLastContextId(), userAccount);
            task.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale, userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", false);
        return "project/id/task/add";
    }

    @RequestMapping(path = "/task/add", method = RequestMethod.POST)
    public final String projectTaskAddPost(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result,
        Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        model.addAttribute("taskstateType",PROJECTS.getType());
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
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
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task.setFocus(false);
            task.setContext(context);
            long maxOrderIdProject = taskService.getMaxOrderIdProject(task.getProject(),context);
            task.setOrderIdProject(++maxOrderIdProject);
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.addToProject(task);
            log.info(task.toString());
            model.addAttribute("userSession", userSession);
            return thisProject.getUrl();
        }
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
        log.info("/project/"+projectId);
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = null;
        Page<Task> taskPage = null;
        if (projectId != 0) {
            thisProject = projectService.findByProjectId(projectId);
            taskPage = taskService.findByProject(thisProject, pageable);
        } else {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setContext(context);
            taskPage = taskService.findByRootProject(context, pageable);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
        }
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return "project/id/show";
    }

    @RequestMapping(path = "/project/add", method = RequestMethod.GET)
    public final String projectAddProjectGet(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("private addNewProjectGet (GET) projectId="+projectId);
        Context context = super.getContext(userSession);
        projectControllerService.addNewProjectToProjectIdForm(projectId, userSession, context, locale, model);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
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
        log.info("private addNewProjectPost (POST) projectId="+projectId+" "+project.toString());
        Context context = super.getContext(userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
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

    @RequestMapping(path = "/project/move/to/project/{targetProjectId}", method = RequestMethod.GET)
    public final String projectMoveToProjectGet(
            @PathVariable("projectId") Project thisProject,
            @PathVariable long targetProjectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model
    ) {
        userSession.setLastProjectId(thisProject.getId());
        model.addAttribute("userSession",userSession);
        Project targetProject = projectService.findByProjectId(targetProjectId);
        thisProject = projectService.moveProjectToAnotherProject(thisProject, targetProject );
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
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
        model.addAttribute("userSession",userSession);
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
        model.addAttribute("contexts", contexts);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", thisProject);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
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
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            thisProject = projectService.findByProjectId(projectId);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("userSession", userSession);
            return "project/id/edit";
        } else {
            thisProject = projectService.findByProjectId(project.getId());
            thisProject.setName(project.getName());
            thisProject.setDescription(project.getDescription());
            Context newContext = project.getContext();
            boolean contextChanged = (newContext.getId().longValue() != thisProject.getContext().getId().longValue());
            if(contextChanged){
                long newContextId = newContext.getId();
                newContext = contextService.findByIdAndUserAccount(newContextId, thisUser);
                thisProject = projectService.moveProjectToAnotherContext(thisProject, newContext);
                userSession.setLastContextId(newContextId);
            } else {
                thisProject = projectService.update(thisProject);
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
            Project parent = projectService.delete(project);
            //TODO: message to message_properties
            String message = "Project is deleted. You see its parent project now.";
            //TODO: message to UserSessionBean userSession
            model.addAttribute("message", message );
            //TODO: isDeleted as message to UserSessionBean userSession
            model.addAttribute("isDeleted",true);
            model.addAttribute("userSession", userSession);
            if(parent == null){
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(project,locale,userSession);
            Page<Task> taskPage = taskService.findByProject(project, request);
            model.addAttribute("message",s.toString());
            model.addAttribute("isDeleted",false);
            model.addAttribute("taskPage", taskPage);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", project);
            model.addAttribute("userSession", userSession);
            model.addAttribute("taskstateType",PROJECTS.getType());
            model.addAttribute("dataPage", true);
            return "project/id/show";
        }
    }

    @RequestMapping(path = "/task/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String moveTaskToTaskAndChangeTaskOrderInProject(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("taskId") Task sourceTask,
        @PathVariable("destinationTaskId") Task destinationTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        userSession.setLastProjectId(thisProject.getId());
        model.addAttribute("userSession",userSession);
        log.info("-------------------------------------------------");
        log.info("  projectTaskChangeOrderToTaskGet");
        log.info("-------------------------------------------------");
        log.info("  source Task:      "+sourceTask.toString());
        log.info("-------------------------------------------------");
        log.info("  destination Task: "+destinationTask.toString());
        log.info("-------------------------------------------------");
        projectControllerService.moveTaskToTaskAndChangeTaskOrderInProject(sourceTask, destinationTask);
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
        model.addAttribute("taskstateType",PROJECTS.getType());
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
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }


    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskGet(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("editTaskGet");
        UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Context thisContext = task.getContext();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("thisContext", thisContext);
        model.addAttribute("task", task);
        model.addAttribute("contexts", contexts);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", true);
        return "project/id/task/edit";
    }

    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.POST)
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
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", true);
        if(task.getTaskState()==TaskState.SCHEDULED && task.getDueDate()==null){
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
            UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
            List<Context> contexts = contextService.getAllForUser(userAccount);
            task = addProject(task);
            Context thisContext = task.getContext();
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
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
            Task persistentTask = addProject(task);
            persistentTask.merge(task);
            task = taskService.updatedViaProject(persistentTask);
            userSession.setLastProjectId(Project.rootProjectId);
            userSession.setLastTaskState(task.getTaskState());
            userSession.setLastTaskId(task.getId());
            model.addAttribute("userSession", userSession);
            return thisProject.getUrl();
        }
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
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
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
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState( task.getTaskState(), task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/setfocus", method = RequestMethod.GET)
    public final String setFocusGet(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back,
        Model model
    ){
        task.setFocus();
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back,
        Model model
    ){
        task.unsetFocus();
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/project/root", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task = taskMoveService.moveTaskToRootProject(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return "redirect:/project/root";
    }

    @RequestMapping(path = "/task/{taskId}/move/to/project/{otherProjectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("taskId") Task task,
        @PathVariable("otherProjectId") Project otherProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task = taskMoveService.moveTaskToAnotherProject(task,otherProject);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return otherProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task.moveToInbox();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
    public final String moveTaskToToday(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to today");
        task.moveToToday();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
    public final String moveTaskToNext(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to next");
        task.moveToNext();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to waiting");
        task.moveToWaiting();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to someday");
        task.moveToSomeday();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to focus");
        task.moveToFocus();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to completed");
        task.moveToCompletedTasks();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/move/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(
        @PathVariable("projectId") Project thisProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @PathVariable("taskId") Task task,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to trash");
        task.moveToTrash();
        task = taskService.updatedViaProject(task);
        userSession.setLastProjectId(thisProject.getId());
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return thisProject.getUrl();
    }

    @RequestMapping(path = "/task/{taskId}/transform", method = RequestMethod.GET)
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
        model.addAttribute("taskstateType",PROJECTS.getType());
        model.addAttribute("dataPage", true);
        return transformTaskIntoProjektService.transformTaskIntoProjectGet(task, userSession, model);
    }
}
