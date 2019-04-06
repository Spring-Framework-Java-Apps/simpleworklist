package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.impl.AbstractController;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.TaskStateService;
import org.woehlke.simpleworklist.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;



/**
 * Created by tw on 21.02.16.
 */
@Controller
@RequestMapping(value = "/taskstate")
public class TaskStateController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStateController.class);

    private final TaskStateService taskStateService;

    private final TaskService taskService;

    @Autowired
    public TaskStateController(TaskStateService taskStateService, TaskService taskService) {
        this.taskStateService = taskStateService;
        this.taskService = taskService;
    }

    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public final String inbox(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId()==0){
            taskPage = taskStateService.getInbox(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getInbox(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        return "taskstate/inbox";
    }

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getToday(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getToday(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "today");
        return "taskstate/today";
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getNext(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getNext(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "next");
        return "taskstate/next";
    }

    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        //TODO: put if to service
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getWaiting(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getWaiting(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "waiting");
        return "taskstate/waiting";
    }

    @RequestMapping(value = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getScheduled(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getScheduled(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "scheduled");
        return "taskstate/scheduled";
    }

    @RequestMapping(value = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getSomeday(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getSomeday(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "someday");
        return "taskstate/someday";
    }

    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getCompleted(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getCompleted(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "completed");
        return "taskstate/completed";
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = null;
        if(userSession.getContextId() == 0){
            taskPage = taskStateService.getTrash(thisUser, pageable);
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
            taskPage = taskStateService.getTrash(thisUser, context, pageable);
        }
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "trash");
        return "taskstate/trash";
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAllTasksForUser(
            @PageableDefault(sort = "lastChangeTimestamp") Pageable request, Model model){
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = taskService.findByUser(userAccount,request);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        return "taskstate/all";
    }

    @RequestMapping(value = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getFocus(context, thisUser, pageable);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "focus");
        return "taskstate/focus";
    }

    @RequestMapping(value = "/move/{taskId}/to/inbox", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/today", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/next", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/waiting", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/someday", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/completed", method = RequestMethod.GET)
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

    @RequestMapping(value = "/move/{taskId}/to/trash", method = RequestMethod.GET)
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

    @RequestMapping(value = "/completed/deleteall", method = RequestMethod.GET)
    public final String deleteallCompleted(
            @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        taskStateService.deleteAllCompleted(context,thisUser);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
            @ModelAttribute("userSession") UserSessionBean userSession,Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        taskService.emptyTrash(userAccount,context);
        return "redirect:/taskstate/trash";
    }

}