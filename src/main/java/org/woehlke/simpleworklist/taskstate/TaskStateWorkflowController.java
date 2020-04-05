package org.woehlke.simpleworklist.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskControllerService;
import org.woehlke.simpleworklist.user.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 21.02.16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/taskstate/task")
public class TaskStateWorkflowController extends AbstractController {

    private final TaskStateService taskStateService;
    private final TaskMoveService taskMoveService;
    private final TaskControllerService taskControllerService;

    @Autowired
    public TaskStateWorkflowController(
        TaskStateService taskStateService,
        TaskMoveService taskMoveService,
        TaskControllerService taskControllerService
    ) {
        this.taskStateService = taskStateService;
        this.taskMoveService = taskMoveService;
        this.taskControllerService = taskControllerService;
    }


    @RequestMapping(value = "/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
        @PathVariable("sourceTaskId") Task sourceTask,
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
        taskMoveService.moveOrderIdTaskState(sourceTask, destinationTask);
        return "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
    }


    @RequestMapping(value = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(@PathVariable("taskId") Task task,
                                                 @PathVariable long projectId) {
        if(projectId == 0) {
            task = taskMoveService.moveTaskToRootProject(task);
        } else {
            Project project = projectService.findByProjectId(projectId);
            task = taskMoveService.moveTaskToAnotherProject(task,project);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task = taskMoveService.moveTaskToInbox(task);
        return "redirect:/taskstate/inbox";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to today");
        task = taskMoveService.moveTaskToToday(task);
        return "redirect:/taskstate/today";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate//next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to next");
        task = taskMoveService.moveTaskToNext(task);
        return "redirect:/taskstate/next";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to waiting");
        task = taskMoveService.moveTaskToWaiting(task);
        return "redirect:/taskstate/waiting";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate//someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to someday");
        task = taskMoveService.moveTaskToSomeday(task);
        return "redirect:/taskstate/someday";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to focus");
        task = taskMoveService.moveTaskToFocus(task);
        return "redirect:/taskstate/focus";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to completed");
        task = taskMoveService.moveTaskToCompleted(task);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(value = "/{taskId}/move/to/taskstate/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to trash");
        task = taskMoveService.moveTaskToTrash(task);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/completed/move/to/taskstate/trash", method = RequestMethod.GET)
    public final String deleteallCompleted(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskMoveService.deleteAllCompleted(context);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskMoveService.emptyTrash(context);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task != null){
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.complete(task);
        }
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/incomplete/", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task !=null) {
            taskService.incomplete(task);
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.saveAndFlush(task);
            return "redirect:/taskstate/"+task.getTaskState().name().toLowerCase();
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/setfocus/", method = RequestMethod.GET)
    public final String setFocusGet(
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back
    ){
        if(task !=null) {
            taskService.setFocus(task);
            return taskControllerService.getView(task,back);
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
            taskService.unsetFocus(task);
            return taskControllerService.getView(task,back);
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

}
