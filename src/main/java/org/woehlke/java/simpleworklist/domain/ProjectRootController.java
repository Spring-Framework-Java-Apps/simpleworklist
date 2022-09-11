package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Slf4j
@Controller
@RequestMapping(path = "/project/root")
public class ProjectRootController extends AbstractController {

    public final static String rootProjectUrl = "redirect:/project/root";

    private final ProjectControllerService projectControllerService;
    private final TaskLifecycleService taskLifecycleService;
    private final TaskMoveService taskMoveService;

    @Autowired
    public ProjectRootController(
      ProjectControllerService projectControllerService,
      TaskMoveService taskMoveService,
      TaskLifecycleService taskLifecycleService
    ) {
      this.projectControllerService = projectControllerService;
      this.taskMoveService = taskMoveService;
      this.taskLifecycleService = taskLifecycleService;
    }

    @RequestMapping(path="", method = RequestMethod.GET)
    public final String projectRoot(
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowProjectRoot(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
        }
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getSlug());
        model.addAttribute("dataPage", true);
        return "project/root/show";
    }

    @RequestMapping(path = "/project/add", method = RequestMethod.GET)
    public final String projectRootAddProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ){
        log.info("/project/root/project/add (GET)");
        Context context = super.getContext(userSession);
        projectControllerService.addNewProjectToProjectRootForm(userSession, context, locale, model);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getSlug());
        model.addAttribute("dataPage", true);
        return "project/root/project/add";
    }

    @RequestMapping(path = "/project/add", method = RequestMethod.POST)
    public final String projectRootAddProjectPost(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ) {
        log.info("/project/root/add/project (POST)");
        Context context = super.getContext(userSession);
        if (result.hasErrors()) {
            model.addAttribute("userSession", userSession);
            return "project/root/project/add";
        } else {
            project.setUuid(UUID.randomUUID());
            return projectControllerService.addNewProjectToProjectRootPersist(
                userSession,
                project,
                context,
                result,
                locale,
                model
            );
        }
    }

    @RequestMapping(path = "/task/add", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/add", method = RequestMethod.POST)
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


    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskGet(
        @PathVariable("taskId") Task thisTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("editTaskGet");
        UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Context thisContext = thisTask.getContext();
        Project thisProject = addProjectFromTaskToModel( thisTask, model );
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

    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.POST)
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
            UserAccount userAccount = loginSuccessService.retrieveCurrentUser();
            List<Context> contexts = contextService.getAllForUser(userAccount);
            thisTask = addProject(thisTask);
            Context thisContext = thisTask.getContext();
            Project thisProject = addProjectFromTaskToModel( thisTask, model );
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
            Task persistentTask  = addProject(thisTask);
            thisTask = taskLifecycleService.updatedViaProjectRoot(persistentTask);
            userSession.setLastProjectId(Project.rootProjectId);
            userSession.setLastTaskState(thisTask.getTaskState());
            userSession.setLastTaskId(thisTask.getId());
            model.addAttribute("userSession", userSession);
            return thisTask.getTaskState().getUrlPathRedirect();
        }
    }

    @RequestMapping(path = "/task/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/project/root", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/move/to/trash", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/completed/move/to/trash", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/trash/empty", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/delete", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/undelete", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/transform", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/complete", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/incomplete", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/setfocus", method = RequestMethod.GET)
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

    @RequestMapping(path = "/task/{taskId}/unsetfocus", method = RequestMethod.GET)
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
