package org.woehlke.simpleworklist.control.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.model.services.TaskMoveService;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.model.services.TaskStateService;

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

    private final TaskMoveService taskMoveService;

    @Autowired
    public TaskStateController(TaskStateService taskStateService, TaskMoveService taskMoveService) {
        this.taskStateService = taskStateService;
        this.taskMoveService = taskMoveService;
    }

    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public final String inbox(
            @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
            @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.INBOX);
        Page<Task> taskPage = taskStateService.getInbox(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        model.addAttribute("userSession", userSession);
        return "taskstate/inbox";
    }

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.TODAY);
        Page<Task> taskPage = taskStateService.getToday(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TODAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "today");
        model.addAttribute("userSession", userSession);
        return "taskstate/today";
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.NEXT);
        Page<Task> taskPage = taskStateService.getNext(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.NEXT,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "next");
        model.addAttribute("userSession", userSession);
        return "taskstate/next";
    }

    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.WAITING);
        Page<Task> taskPage = taskStateService.getWaiting(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.WAITING,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "waiting");
        model.addAttribute("userSession", userSession);
        return "taskstate/waiting";
    }

    @RequestMapping(value = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.SCHEDULED);
        Page<Task> taskPage = taskStateService.getScheduled(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SCHEDULED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "scheduled");
        model.addAttribute("userSession", userSession);
        return "taskstate/scheduled";
    }

    @RequestMapping(value = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.SOMEDAY);
        Page<Task> taskPage = taskStateService.getSomeday(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SOMEDAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "someday");
        model.addAttribute("userSession", userSession);
        return "taskstate/someday";
    }

    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.COMPLETED);
        Page<Task> taskPage = taskStateService.getCompleted(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.COMPLETED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "completed");
        model.addAttribute("userSession", userSession);
        return "taskstate/completed";
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.TRASH);
        Page<Task> taskPage = taskStateService.getTrash(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TRASH,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "trash");
        model.addAttribute("userSession", userSession);
        return "taskstate/trash";
    }

    @RequestMapping(value = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(TaskState.FOCUS);
        Page<Task> taskPage = taskStateService.getFocus(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.FOCUS,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "focus");
        model.addAttribute("userSession", userSession);
        return "taskstate/focus";
    }

    @RequestMapping(value = "/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
            @PathVariable("sourceTaskId") Task sourceTask,
            @PathVariable("destinationTaskId") Task destinationTask,
            @ModelAttribute("userSession") UserSessionBean userSession, Model model
    ){
        Context context = super.getContext(userSession);
        userSession.setLastTaskState(sourceTask.getTaskState());
        model.addAttribute("userSession", userSession);
        LOGGER.info("------------- changeTaskOrderId -------------");
        LOGGER.info("source Task:      "+sourceTask.toString());
        LOGGER.info("---------------------------------------------");
        LOGGER.info("destination Task: "+destinationTask.toString());
        LOGGER.info("---------------------------------------------");
        taskMoveService.moveOrderIdTaskState(sourceTask, destinationTask);
        return "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
    }

}