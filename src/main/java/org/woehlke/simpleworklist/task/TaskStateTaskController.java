package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.application.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.application.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        task.unsetFocus();
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
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        @NotNull @Valid Task task,
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
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
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
        @NotNull @Valid Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
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
        @NotNull @PathVariable("taskId") Task sourceTask,
        @NotNull @PathVariable("destinationTaskId") Task destinationTask,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
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
        return sourceTask.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @PathVariable("projectId") Project project
    ) {
        task = taskService.moveTaskToAnotherProject(task,project);
        return project.getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task.moveToInbox();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to today");
        task.moveToToday();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to next");
        task.moveToNext();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to waiting");
        task.moveToWaiting();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to someday");
        task.moveToSomeday();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to focus");
        task.moveToFocus();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to completed");
        task.moveToCompletedTasks();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@NotNull @PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to trash");
        task.moveToTrash();
        task = taskService.updatedViaTaskstate(task);
        return task.getTaskState().getUrl();
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
    public final String deleteTaskGet(@NotNull @PathVariable("taskId") Task task) {
        log.info("deleteTaskGet");
        if(task!= null){
            task.delete();
            taskService.updatedViaTaskstate(task);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/{taskId}/undelete", method = RequestMethod.GET)
    public final String undeleteTaskGet(@NotNull @PathVariable("taskId") Task task) {
        log.info("undeleteTaskGet");
        task.undelete();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/transform", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(@NotNull @PathVariable("taskId") Task task) {
        log.info("transformTaskIntoProjectGet");
        return taskStateControllerService.transformTaskIntoProjectGet(task);
    }

    @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @NotNull @PathVariable("taskId") Task task
    ) {
        task.complete();
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/incomplete", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @NotNull @PathVariable("taskId") Task task
    ) {
        task.incomplete();
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/setfocus", method = RequestMethod.GET)
    public final String setFocusGet(
        @NotNull @PathVariable("taskId") Task task
    ){
        task.setFocus();
        task = taskService.updatedViaTaskstate(task);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @NotNull @PathVariable("taskId") Task task
    ){
      task.unsetFocus();
      task = taskService.updatedViaTaskstate(task);
      return task.getUrl();
    }
}
