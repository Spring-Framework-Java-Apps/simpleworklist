package org.woehlke.java.simpleworklist.domain;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.java.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.project.Project;
import org.woehlke.java.simpleworklist.domain.task.Task;
import org.woehlke.java.simpleworklist.domain.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.task.TaskService;
import org.woehlke.java.simpleworklist.domain.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.taskworkflow.MoveTaskToTaskInTaskstateService;
import org.woehlke.java.simpleworklist.domain.taskworkflow.TaskState;
import org.woehlke.java.simpleworklist.domain.taskworkflow.TransformTaskIntoProjektService;
import org.woehlke.java.simpleworklist.domain.user.account.UserAccount;
import org.woehlke.java.simpleworklist.application.framework.AbstractController;
import org.woehlke.java.simpleworklist.domain.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

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

    private final MoveTaskToTaskInTaskstateService moveTaskToTaskInTaskstateService;
    private final TaskService taskService;
    private final TransformTaskIntoProjektService transformTaskIntoProjektService;

    @Autowired
    public TaskStateTaskController(
        MoveTaskToTaskInTaskstateService moveTaskToTaskInTaskstateService, TaskService taskService,
        TransformTaskIntoProjektService transformTaskIntoProjektService) {
        this.moveTaskToTaskInTaskstateService = moveTaskToTaskInTaskstateService;
        this.taskService = taskService;
        this.transformTaskIntoProjektService = transformTaskIntoProjektService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public final String addNewTaskToInboxGet(
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX, locale, userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseContext);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", false);
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
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", false);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Boolean mustChooseArea = false;
            task.setContext(context);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX, locale, userSession);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            model.addAttribute("userSession", userSession);
            return "taskstate/task/add";
        } else {
            task = taskService.addToInbox(task);
            log.info(task.toString());
            model.addAttribute("userSession", userSession);
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
        addProjectFromTaskToModel( task, model );
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
        Context thisContext = task.getContext();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(task.getTaskState(), locale, userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisContext", thisContext);
        model.addAttribute("task", task);
        model.addAttribute("contexts", contexts);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        model.addAttribute("addProjectToTask", true);
        return "taskstate/task/edit";
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
            //Task persistentTask = taskService.findOne(taskId);
            task = addProject(task);
            UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
            List<Context> contexts = contextService.getAllForUser(userAccount);
            Project thisProject = addProjectFromTaskToModel( task, model );
            Context thisContext = task.getContext();
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale,userSession);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisContext", thisContext);
            model.addAttribute("task", task);
            model.addAttribute("contexts", contexts);
            model.addAttribute("userSession", userSession);
            return "taskstate/task/edit";
        } else {
            task.unsetFocus();
            Task persistentTask = addProject(task);
            task = taskService.updatedViaTaskstate(persistentTask);
            model.addAttribute("userSession", userSession);
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
        log.info("------------- changeTaskOrderId -------------");
        log.info("source Task:      "+sourceTask.toString());
        log.info("---------------------------------------------");
        log.info("destination Task: "+destinationTask.toString());
        log.info("---------------------------------------------");
        moveTaskToTaskInTaskstateService.moveTaskToTaskAndChangeTaskOrderInTaskstate(sourceTask, destinationTask);
        userSession.setLastTaskState(sourceTask.getTaskState());
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return sourceTask.getTaskState().getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @PathVariable("projectId") Project project,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task = taskService.moveTaskToAnotherProject(task,project);
        userSession.setLastProjectId(project.getId());
        model.addAttribute("userSession",userSession);
        model.addAttribute("dataPage", true);
        return project.getUrl();
    }

    @RequestMapping(path = "/{taskId}/move/to/project/root", method = RequestMethod.GET)
    public final String moveTaskToRootProject(
      @NotNull @PathVariable("taskId") Task task,
      @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
      Model model
    ) {
      task = taskService.moveTaskToRootProject(task);
      userSession.setLastProjectId(0L);
      model.addAttribute("userSession",userSession);
      model.addAttribute("dataPage", true);
      return "redirect:/project/root";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task = taskService.moveTaskToInbox(task);
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
        task = taskService.moveTaskToToday(task);
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
        task = taskService.moveTaskToNext(task);
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
        task = taskService.moveTaskToWaiting(task);
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
        task = taskService.moveTaskToSomeday(task);
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
        task = taskService.moveTaskToFocus(task);
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
        task = taskService.moveTaskToCompleted(task);
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
        task = taskService.moveTaskToTrash(task);
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
        taskService.moveAllCompletedToTrash(context);
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
        taskService.emptyTrash(context);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return "redirect:/taskstate/trash";
    }


    @RequestMapping(path = "/{taskId}/delete", method = RequestMethod.GET)
    public final String deleteTaskGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        log.info("deleteTaskGet");
        task.delete();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/{taskId}/undelete", method = RequestMethod.GET)
    public final String undeleteTaskGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        log.info("undeleteTaskGet");
        task.undelete();
        taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/transform", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        log.info("transformTaskIntoProjectGet");
      model.addAttribute("dataPage", true);
        return transformTaskIntoProjektService.transformTaskIntoProjectGet(task, userSession, model);
    }

    @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task.complete();
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/incomplete", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ) {
        task.incomplete();
        long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/setfocus", method = RequestMethod.GET)
    public final String setFocusGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        task.setFocus();
        task = taskService.updatedViaTaskstate(task);
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
        return task.getUrl();
    }

    @RequestMapping(path = "/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @NotNull @PathVariable("taskId") Task task,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
      task.unsetFocus();
      task = taskService.updatedViaTaskstate(task);
      model.addAttribute("userSession", userSession);
      model.addAttribute("dataPage", true);
      return task.getUrl();
    }
}
