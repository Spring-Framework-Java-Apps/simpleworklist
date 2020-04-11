package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by tw on 21.02.16.
 */
@Slf4j
@Controller
@RequestMapping(path = "/taskstate/task")
public class TaskStateTaskController extends AbstractController {


    private final TaskStateControllerService taskStateControllerService;
    private final TaskService taskService;

    @Autowired
    public TaskStateTaskController(
        TaskStateControllerService taskStateControllerService, TaskService taskService
    ) {
        this.taskStateControllerService = taskStateControllerService;
        this.taskService = taskService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public final String addNewTaskToInboxGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("addNewTaskToInboxGet");
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Boolean mustChooseContext = false;
        if(userSession.getLastContextId() == 0L){
            mustChooseContext = true;
            task.setContext(userAccount.getDefaultContext());
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getLastContextId(), userAccount);
            task.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseContext);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "taskstate/task/add";
    }

    @RequestMapping(path = "/add",  method = RequestMethod.POST)
    public final String addNewTaskToInboxPost(
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result,
        Locale locale,
        Model model
    ) {
        log.info("addNewTaskToInboxPost");
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
            return "taskstate/task/add";
        } else {
            task = taskService.addToInbox(task);
            log.info(task.toString());
            return "redirect:/taskstate/" + task.getTaskState().name().toLowerCase();
        }
    }

    @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("editTaskGet");
        if(task != null) {
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(task.getTaskState(),locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("thisContext", thisContext);
            model.addAttribute("task", task);
            model.addAttribute("contextss", contexts);
            return "taskstate/task/edit";
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.POST)
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
                log.error(e.toString());
            }
            Task persistentTask = taskService.findOne(taskId);
            persistentTask.merge(task);
            task = persistentTask;
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("thisContext", thisContext);
            model.addAttribute("task", task);
            model.addAttribute("contexts", contexts);
            return "taskstate/task/edit";
        } else {
            task.unsetFocus();
            task.setRootProject();
            Task persistentTask = taskService.findOne(task.getId());
            persistentTask.merge(task);
            task = taskService.updatedViaTaskstate(persistentTask);
            return task.getTaskState().getUrl();
        }
    }

    @RequestMapping(path = "/{taskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
        @PathVariable("taskId") Task sourceTask,
        @PathVariable("destinationTaskId") Task destinationTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        userSession.setLastTaskState(sourceTask.getTaskState());
        model.addAttribute("userSession", userSession);
        log.info("------------- changeTaskOrderId -------------");
        log.info("source Task:      "+sourceTask.toString());
        log.info("---------------------------------------------");
        log.info("destination Task: "+destinationTask.toString());
        log.info("---------------------------------------------");
        taskService.moveOrderIdTaskState(sourceTask, destinationTask);
        return "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
    }

    @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @PathVariable("taskId") Task task,
        @PathVariable long projectId
    ) {
        if(projectId == 0) {
            task = taskService.moveTaskToRootProject(task);
        } else {
            Project project = projectService.findByProjectId(projectId);
            task = taskService.moveTaskToAnotherProject(task,project);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task.moveToInbox();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/inbox";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to today");
        task.moveToToday();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/today";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to next");
        task.moveToNext();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/next";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to waiting");
        task.moveToWaiting();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/waiting";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to someday");
        task.moveToSomeday();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/someday";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to focus");
        task.moveToFocus();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/focus";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to completed");
        task.moveToCompletedTasks();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/move/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to trash");
        task.moveToTrash();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/completed/move/to/trash", method = RequestMethod.GET)
    public final String moveAllCompletedToTrash(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskService.moveAllCompletedToTrash(context);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskService.emptyTrash(context);
        return "redirect:/taskstate/trash";
    }


    @RequestMapping(path = "/{taskId}/delete", method = RequestMethod.GET)
    public final String deleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("deleteTaskGet");
        if(task!= null){
            task.delete();
            taskService.updatedViaTaskstate(task);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/{taskId}/undelete", method = RequestMethod.GET)
    public final String undeleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("undeleteTaskGet");
        if(task!= null) {
            task.undelete();
            taskService.updatedViaTaskstate(task);
            return "redirect:/taskstate/completed";
        } else {
            return "redirect:/taskstate/trash";
        }
    }

    @RequestMapping(path = "/{taskId}/transform", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(@PathVariable("taskId") Task task) {
        log.info("transformTaskIntoProjectGet");
        return taskStateControllerService.transformTaskIntoProjectGet(task);
    }

    @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task != null){
            task.complete();
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.updatedViaTaskstate(task);
            return task.getUrl();
        }
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/incomplete", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task !=null) {
            task.incomplete();
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.updatedViaTaskstate(task);
            return task.getUrl();
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/setfocus", method = RequestMethod.GET)
    public final String setFocusGet(
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back
    ){
        if(task !=null) {
            task.setFocus();
            task = taskService.updatedViaTaskstate(task);
            return task.getUrl();
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back
    ){
        if(task !=null) {
            task.unsetFocus();
            task = taskService.updatedViaTaskstate(task);
            return task.getUrl();
        } else {
            return "redirect:/taskstate/inbox";
        }
    }
}
