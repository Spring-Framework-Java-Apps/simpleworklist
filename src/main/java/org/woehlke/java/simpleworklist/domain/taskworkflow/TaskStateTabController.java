package org.woehlke.java.simpleworklist.domain.taskworkflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.java.simpleworklist.application.framework.AbstractController;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.session.UserSessionBean;

import lombok.extern.slf4j.Slf4j;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(path = "/taskstate")
public class TaskStateTabController extends AbstractController {

    private final MoveTaskToTaskInTaskstateService moveTaskToTaskInTaskstateService;

    @Autowired
    public TaskStateTabController(MoveTaskToTaskInTaskstateService moveTaskToTaskInTaskstateService) {
        this.moveTaskToTaskInTaskstateService = moveTaskToTaskInTaskstateService;
    }

    @RequestMapping(path = "/inbox", method = RequestMethod.GET)
    public final String inbox(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.INBOX,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/today", method = RequestMethod.GET)
    public final String today(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.TODAY,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/next", method = RequestMethod.GET)
    public final String next(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.NEXT,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/waiting", method = RequestMethod.GET)
    public final String waiting(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.WAITING,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/scheduled", method = RequestMethod.GET)
    public final String scheduled(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(TaskState.SCHEDULED, context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/someday", method = RequestMethod.GET)
    public final String someday(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.SOMEDAY,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/completed", method = RequestMethod.GET)
    public final String completed(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.COMPLETED,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/trash", method = RequestMethod.GET)
    public final String trash(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.TRASH,
            context, pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/deleted", method = RequestMethod.GET)
    public final String deleted(
      @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
      @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
      Locale locale,
      Model model
    ) {
      return trash(pageable, userSession, locale, model);
    }

    @RequestMapping(path = "/focus", method = RequestMethod.GET)
    public final String focus(
        @PageableDefault(sort = "orderIdTaskState", direction = Sort.Direction.DESC) Pageable pageable,
        @NotNull @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        Context context = super.getContext(userSession);
        return moveTaskToTaskInTaskstateService.getTaskStatePage(
            TaskState.FOCUS,
            context, pageable, userSession, locale, model);
    }
}
