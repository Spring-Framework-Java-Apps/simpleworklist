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
import org.woehlke.simpleworklist.oodm.services.TaskService;

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
        Page<Task> taskPage = taskStateService.getInbox(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "inbox");
        return "taskstate/inbox";
    }

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getToday(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TODAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "today");
        return "taskstate/today";
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getNext(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.NEXT,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "next");
        return "taskstate/next";
    }

    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getWaiting(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.WAITING,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "waiting");
        return "taskstate/waiting";
    }

    @RequestMapping(value = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getScheduled(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SCHEDULED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "scheduled");
        return "taskstate/scheduled";
    }

    @RequestMapping(value = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getSomeday(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.SOMEDAY,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "someday");
        return "taskstate/someday";
    }

    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getCompleted(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.COMPLETED,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "completed");
        return "taskstate/completed";
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getTrash(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.TRASH,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "trash");
        return "taskstate/trash";
    }

    @RequestMapping(value = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession, Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getFocus(context, pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.FOCUS,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", "focus");
        return "taskstate/focus";
    }

    @RequestMapping(value = "/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
            @PathVariable("sourceTaskId") Task sourceTask,
            @PathVariable("destinationTaskId") Task destinationTask,
            @ModelAttribute("userSession") UserSessionBean userSession
    ){
        LOGGER.info("------------- changeTaskOrderId -------------");
        LOGGER.info("source Task:      "+sourceTask.toString());
        LOGGER.info("---------------------------------------------");
        LOGGER.info("destination Task: "+destinationTask.toString());
        LOGGER.info("---------------------------------------------");
        taskMoveService.moveOrderIdTaskState(sourceTask, destinationTask);
        return "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
    }

}