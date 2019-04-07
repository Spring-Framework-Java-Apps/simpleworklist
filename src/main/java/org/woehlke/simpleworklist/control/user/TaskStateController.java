package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.model.Breadcrumb;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.model.services.TaskStateService;
import org.woehlke.simpleworklist.model.services.TaskService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


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
            @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getInbox(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        return "taskstate/inbox";
    }

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getToday(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TODAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "today");
        return "taskstate/today";
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getNext(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.NEXT,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "next");
        return "taskstate/next";
    }

    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getWaiting(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.WAITING,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "waiting");
        return "taskstate/waiting";
    }

    @RequestMapping(value = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getScheduled(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SCHEDULED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "scheduled");
        return "taskstate/scheduled";
    }

    @RequestMapping(value = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getSomeday(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SOMEDAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "someday");
        return "taskstate/someday";
    }

    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getCompleted(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.COMPLETED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "completed");
        return "taskstate/completed";
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getTrash(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TRASHED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "trash");
        return "taskstate/trash";
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAllTasksForUser(
            @PageableDefault(sort = "lastChangeTimestamp") Pageable request,
            @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ){
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskService.findByUser(thisUser, context, request);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstateAll(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        return "taskstate/all";
    }

    @RequestMapping(value = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Page<Task> taskPage = taskStateService.getFocus(thisUser, context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.FOCUS,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "focus");
        return "taskstate/focus";
    }

    @RequestMapping(value = "/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
            @PathVariable long sourceTaskId,
            @PathVariable long destinationTaskId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Model model){
        UserAccount thisUser = userAccountLoginSuccessService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), thisUser);
        Task sourceTask = taskService.findOne(sourceTaskId,thisUser);
        Task destinationTask = taskService.findOne(destinationTaskId,thisUser);
        LOGGER.info("------------- changeTaskOrderId -------------");
        LOGGER.info("source Task:      "+sourceTask.toString());
        LOGGER.info("---------------------------------------------");
        LOGGER.info("destination Task: "+destinationTask.toString());
        LOGGER.info("---------------------------------------------");
        String returnUrl = "redirect:/taskstate/inbox";
        if(sourceTask.getUserAccount().getId().longValue()==destinationTask.getUserAccount().getId().longValue()) {
            boolean sameTaskType = (sourceTask.getTaskState().ordinal() == destinationTask.getTaskState().ordinal());
            if (sameTaskType) {
                taskService.moveOrderIdTaskState(sourceTask.getTaskState(), sourceTask, destinationTask, context);
                returnUrl = "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
            }
        }
        return returnUrl;
    }

}