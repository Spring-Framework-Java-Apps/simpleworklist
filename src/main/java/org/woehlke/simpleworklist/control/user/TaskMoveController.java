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
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.model.services.TaskMoveService;
import org.woehlke.simpleworklist.oodm.services.TaskService;

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
    public final String moveTaskToAnotherProject(@PathVariable("taskId") Task task,
                                                 @PathVariable long projectId) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, userAccount);
        if(projectId == 0) {
            task = taskMoveService.moveTaskToRootProject(task);
        } else {
            Project project = projectService.findByProjectId(projectId, thisUser);
            task = taskMoveService.moveTaskToAnotherProject(task,project);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/{taskId}/to/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to inbox");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId,thisUser);
        task = taskMoveService.moveTaskToInbox(task);
        return "redirect:/taskstate/inbox";
    }

    @RequestMapping(value = "/{taskId}/to/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to today");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToToday(task);
        return "redirect:/taskstate/today";
    }

    @RequestMapping(value = "/{taskId}/to/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to next");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToNext(task);
        return "redirect:/taskstate/next";
    }

    @RequestMapping(value = "/{taskId}/to/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to waiting");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToWaiting(task);
        return "redirect:/taskstate/waiting";
    }

    @RequestMapping(value = "/{taskId}/to/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to someday");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToSomeday(task);
        return "redirect:/taskstate/someday";
    }

    @RequestMapping(value = "/{taskId}/to/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to focus");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToFocus(task);
        return "redirect:/taskstate/focus";
    }

    @RequestMapping(value = "/{taskId}/to/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to completed");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToCompleted(task);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(value = "/{taskId}/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable("taskId") Task task) {
        LOGGER.info("dragged and dropped "+task.getId()+" to trash");
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        //Task task = taskService.findOne(taskId, thisUser);
        task = taskMoveService.moveTaskToTrash(task);
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
            @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        taskMoveService.emptyTrash(thisUser,context);
        return "redirect:/taskstate/trash";
    }
}
