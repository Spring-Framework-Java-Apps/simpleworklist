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
import org.woehlke.simpleworklist.model.Breadcrumb;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.model.services.TaskStateService;
import org.woehlke.simpleworklist.model.services.TaskService;

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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("inbox");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("today");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("next");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("waiting");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("scheduled");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("someday");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("completed");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("trash");
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "trash");
        return "taskstate/trash";
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAllTasksForUser(
            @PageableDefault(sort = "lastChangeTimestamp") Pageable request, Model model){
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Page<Task> taskPage = taskService.findByUser(userAccount,request);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("all");
        model.addAttribute("breadcrumb", breadcrumb);
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate("focus");
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "focus");
        return "taskstate/focus";
    }

}