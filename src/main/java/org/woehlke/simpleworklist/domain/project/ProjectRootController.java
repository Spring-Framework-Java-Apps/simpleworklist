package org.woehlke.simpleworklist.domain.project;

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
import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.application.framework.AbstractController;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskEnergy;
import org.woehlke.simpleworklist.domain.taskworkflow.TaskState;
import org.woehlke.simpleworklist.domain.task.TaskTime;
import org.woehlke.simpleworklist.domain.taskworkflow.TransformTaskIntoProjektService;
import org.woehlke.simpleworklist.domain.task.TaskService;
import org.woehlke.simpleworklist.application.session.UserSessionBean;
import org.woehlke.simpleworklist.domain.user.account.UserAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.woehlke.simpleworklist.domain.project.Project.rootProjectId;

@Slf4j
@Controller
@RequestMapping(path = "/project/root")
public class ProjectRootController extends AbstractController {

    public final static String rootProjectUrl = "redirect:/project/root";

    private final ProjectControllerService projectControllerService;
    private final TaskService taskService;
    private final TransformTaskIntoProjektService transformTaskIntoProjektService;

    @Autowired
    public ProjectRootController(ProjectControllerService projectControllerService, TaskService taskService, TransformTaskIntoProjektService transformTaskIntoProjektService) {
        this.projectControllerService = projectControllerService;
        this.taskService = taskService;
        this.transformTaskIntoProjektService = transformTaskIntoProjektService;
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
            model.addAttribute("myTaskState","PROJECT");
        }
        model.addAttribute("userSession", userSession);
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
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        task.unsetFocus();
        Project thisProject;
        Boolean mustChooseContext = false;
        thisProject = new Project();
        thisProject.setId(0L);
        if(userSession.getLastContextId() == 0L){
            mustChooseContext = true;
            task.setContext(userAccount.getDefaultContext());
            thisProject.setContext(userAccount.getDefaultContext());
        } else {
            task.setContext(context);
            thisProject.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseContext); //TODO: rename mustChooseArea -> mustChooseContext
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("thisProjectId", thisProject.getId());
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        model.addAttribute("userSession", userSession);
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
            task = taskService.addToRootProject(task);
            log.info(task.toString());
            model.addAttribute("userSession", userSession);
            return rootProjectUrl;
        }
    }


    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("editTaskGet");
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Project thisProject;
        if (task.getContext() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = task.getProject();
        }
        Context thisContext = task.getContext();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(task.getTaskState(),locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject); //TODO: remove?
        model.addAttribute("thisContext", thisContext);
        model.addAttribute("task", task);
        model.addAttribute("contexts", contexts);
        model.addAttribute("userSession", userSession);
        return "project/root/task/edit";
    }

    @RequestMapping(path = "/task/{taskId}/edit", method = RequestMethod.POST)
    public final String editTaskPost(
        @PathVariable long taskId,
        @Valid Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale,
        Model model
    ) {
        log.info("editTaskPost");
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
            UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
            List<Context> contexts = contextService.getAllForUser(userAccount);
            Task persistentTask = taskService.findOne(taskId);
            persistentTask.merge(task);
            task = persistentTask;
            Context thisContext = task.getContext();
            Project thisProject = new Project();
            thisProject.setId(0L);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject); //TODO: remove?
            model.addAttribute("thisContext", thisContext);
            model.addAttribute("task", task);
            model.addAttribute("contexts", contexts);
            userSession.setLastProjectId(thisProject.getId());
            userSession.setLastTaskState(task.getTaskState());
            userSession.setLastTaskId(task.getId());
            userSession.setLastContextId(thisContext.getId());
            model.addAttribute("userSession", userSession);
            return "project/root/task/edit";
        } else {
            //task.unsetFocus();
            task.setRootProject();
            Task persistentTask = taskService.findOne(task.getId());
            persistentTask.merge(task);
            task = taskService.updatedViaProjectRoot(persistentTask);
            userSession.setLastProjectId(rootProjectId);
            userSession.setLastTaskState(task.getTaskState());
            userSession.setLastTaskId(task.getId());
            model.addAttribute("userSession", userSession);
            return task.getTaskState().getUrl();
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
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(sourceTask.getTaskState());
        userSession.setLastTaskId(sourceTask.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/move/to/project/root", method = RequestMethod.GET)
    public final String moveTaskToProjectRoot(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task = taskService.moveTaskToRootProject(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToProject(
        @PathVariable("taskId") Task task,
        @PathVariable("projectId") Project targetProject,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task = taskService.moveTaskToAnotherProject(task,targetProject);
        userSession.setLastProjectId(targetProject.getId());
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/completed/move/to/trash", method = RequestMethod.GET)
    public final String moveAllCompletedToTrash(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        Context context = super.getContext(userSession);
        taskService.moveAllCompletedToTrash(context);
        userSession.setLastContextId(context.getId());
        userSession.setLastProjectId(rootProjectId);
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        Context context = super.getContext(userSession);
        taskService.emptyTrash(context);
        userSession.setLastContextId(context.getId());
        userSession.setLastProjectId(rootProjectId);
        model.addAttribute("userSession", userSession);
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
            taskService.updatedViaProjectRoot(task);
        }
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
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
        taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/transform", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        log.info("transformTaskIntoProjectGet");
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        return transformTaskIntoProjektService.transformTaskIntoProjectGet(task, userSession, model);
    }

    @RequestMapping(path = "/task/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task.complete();
        //long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
        //task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/incomplete", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
       task.incomplete();
       //long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
       //task.setOrderIdTaskState(++maxOrderIdTaskState);
       task = taskService.updatedViaProjectRoot(task);
       userSession.setLastProjectId(rootProjectId);
       userSession.setLastTaskState(task.getTaskState());
       userSession.setLastTaskId(task.getId());
       model.addAttribute("userSession", userSession);
       return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/setfocus", method = RequestMethod.GET)
    public final String setFocusGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        task.setFocus();
        task = taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }

    @RequestMapping(path = "/task/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        task.unsetFocus();
        task = taskService.updatedViaProjectRoot(task);
        userSession.setLastProjectId(rootProjectId);
        userSession.setLastTaskState(task.getTaskState());
        userSession.setLastTaskId(task.getId());
        model.addAttribute("userSession", userSession);
        return rootProjectUrl;
    }
}
