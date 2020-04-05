package org.woehlke.simpleworklist.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskControllerService;
import org.woehlke.simpleworklist.task.TaskState;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.user.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


/**
 * Created by tw on 21.02.16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/taskstate")
public class TaskStateController extends AbstractController {

    private final TaskStateService taskStateService;
    private final TaskMoveService taskMoveService;
    private final TaskControllerService taskControllerService;

    @Autowired
    public TaskStateController(
        TaskStateService taskStateService,
        TaskMoveService taskMoveService,
        TaskControllerService taskControllerService
    ) {
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

    @RequestMapping(path = "/complete/{taskId}", method = RequestMethod.GET)
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

    @RequestMapping(path = "/incomplete/{taskId}", method = RequestMethod.GET)
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

    @RequestMapping(path = "/setfocus/{taskId}", method = RequestMethod.GET)
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

    @RequestMapping(path = "/unsetfocus/{taskId}", method = RequestMethod.GET)
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
