package org.woehlke.simpleworklist.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskControllerService;
import org.woehlke.simpleworklist.user.UserSessionBean;

import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(value = "/taskstate")
public class TaskStateDisplayController extends AbstractController {


    private final TaskStateService taskStateService;
    private final TaskMoveService taskMoveService;
    private final TaskControllerService taskControllerService;

    @Autowired
    public TaskStateDisplayController(TaskStateService taskStateService, TaskMoveService taskMoveService, TaskControllerService taskControllerService) {
        this.taskStateService = taskStateService;
        this.taskMoveService = taskMoveService;
        this.taskControllerService = taskControllerService;
    }

    @RequestMapping(value = "/inbox", method = RequestMethod.GET)
    public final String inbox(
        @PageableDefault(sort = "orderIdTaskState") Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getInbox(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.INBOX, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getToday(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.TODAY, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getNext(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.NEXT, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getWaiting(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.WAITING, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getScheduled(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.SCHEDULED, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getSomeday(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.SOMEDAY, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getCompleted(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.COMPLETED, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getTrash(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.TRASH, taskPage, userSession, locale, model);
    }

    @RequestMapping(value = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        Page<Task> taskPage = taskStateService.getFocus(context, pageable);
        return taskControllerService.getTaskStatePage(TaskState.FOCUS, taskPage, userSession, locale, model);
    }
}
