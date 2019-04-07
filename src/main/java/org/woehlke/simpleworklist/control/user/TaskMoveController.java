package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.model.services.TaskMoveService;
import org.woehlke.simpleworklist.model.services.TaskService;
import org.woehlke.simpleworklist.model.services.TaskStateService;

@Controller
@RequestMapping(value = "/task/move")
public class TaskMoveController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskMoveController.class);

    private final TaskMoveService taskMoveService;

    private final TaskService taskService;

    @Autowired
    public TaskMoveController(TaskMoveService taskMoveService, TaskService taskService) {
        this.taskMoveService = taskMoveService;
        this.taskService = taskService;
    }

    @RequestMapping(value = "/{taskId}/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(@PathVariable long taskId,
                                                 @PathVariable long projectId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!=null){
            Project project = projectService.findByProjectId(projectId, userAccount);
            task.setProject(project);
            long maxOrderIdProject = taskService.getMaxOrderIdProject(task.getProject(),task.getContext(),userAccount);
            task.setOrderIdProject(++maxOrderIdProject);
            taskService.saveAndFlush(task, userAccount);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/{taskId}/to/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to inbox");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId,thisUser);
        if(task!=null){
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.INBOX,task.getContext(),thisUser);
            task.setTaskState(TaskState.INBOX);
            task.setOrderIdTaskState(++maxOrderId);
            task=taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped "+taskId+" to inbox: "+task.toString());
        }
        return "redirect:/taskstate/inbox";
    }

    @RequestMapping(value = "/{taskId}/to/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to today");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.TODAY,task.getContext(),thisUser);
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.TODAY);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to today: " + task.toString());
        }
        return "redirect:/taskstate/today";
    }

    @RequestMapping(value = "/{taskId}/to/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to next");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.NEXT,task.getContext(),thisUser);
        task.setOrderIdTaskState(++maxOrderId);
        task.setTaskState(TaskState.NEXT);
        task=taskService.saveAndFlush(task, thisUser);
        LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        return "redirect:/taskstate/next";
    }

    @RequestMapping(value = "/{taskId}/to/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to waiting");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null){
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.WAITING,task.getContext(),thisUser);
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.WAITING);
            task=taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        }
        return "redirect:/taskstate/waiting";
    }

    @RequestMapping(value = "/{taskId}/to/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to someday");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.SOMEDAY,task.getContext(),thisUser);
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.SOMEDAY);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to someday: " + task.toString());
        }
        return "redirect:/taskstate/someday";
    }

    @RequestMapping(value = "/{taskId}/to/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to completed");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext(),thisUser);
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.COMPLETED);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to completed: " + task.toString());
        }
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(value = "/{taskId}/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable long taskId) {
        LOGGER.info("dragged and dropped "+taskId+" to trash");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            long maxOrderId = taskService.getMaxOrderIdTaskState(TaskState.TRASHED,task.getContext(),thisUser);
            task.setOrderIdTaskState(++maxOrderId);
            task.setTaskState(TaskState.TRASHED);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to trash: " + task.toString());
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/completed/to/trash", method = RequestMethod.GET)
    public final String deleteallCompleted(
            @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        taskMoveService.deleteAllCompleted(context,thisUser);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/trash/to/void", method = RequestMethod.GET)
    public final String emptyTrash(
            @ModelAttribute("userSession") UserSessionBean userSession, Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        taskMoveService.emptyTrash(userAccount,context);
        return "redirect:/taskstate/trash";
    }
}
