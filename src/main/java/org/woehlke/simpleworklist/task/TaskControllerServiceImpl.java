package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.user.UserSessionBean;

import java.util.Locale;

@Slf4j
@Service
public class TaskControllerServiceImpl implements TaskControllerService {

    private final BreadcrumbService breadcrumbService;
    private final TaskService taskService;

    @Autowired
    public TaskControllerServiceImpl(
        BreadcrumbService breadcrumbService,
        TaskService taskService
    ) {
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
    }

    @Override
    public String getTaskStatePage(
        TaskState taskState,
        Context context,
        Pageable pageRequest,
        UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.info("getTaskStatePage");
        userSession.setLastTaskState(taskState);
        Page<Task> taskPage = taskService.findbyTaskstate(taskState, context, pageRequest);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", taskState.name().toLowerCase());
        model.addAttribute("userSession", userSession);
        return taskState.getUrl();
    }

}
