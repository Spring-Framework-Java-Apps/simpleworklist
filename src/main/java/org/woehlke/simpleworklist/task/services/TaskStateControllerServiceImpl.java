package org.woehlke.simpleworklist.task.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.application.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.application.breadcrumb.services.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskState;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Slf4j
@Service
public class TaskStateControllerServiceImpl implements TaskStateControllerService {

    private final BreadcrumbService breadcrumbService;
    private final TaskService taskService;

    @Autowired
    public TaskStateControllerServiceImpl(
        BreadcrumbService breadcrumbService,
        TaskService taskService
    ) {
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
    }

    @Override
    public String getTaskStatePage(
        @NotNull TaskState taskState,
        @NotNull Context context,
        @NotNull Pageable pageRequest,
        @NotNull UserSessionBean userSession,
        @NotNull Locale locale,
        @NotNull Model model
    ){
        log.info("getTaskStatePage");
        userSession.setLastTaskState(taskState);
        Page<Task> taskPage = taskService.findbyTaskstate(taskState, context, pageRequest);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", taskState.getType() );
        model.addAttribute("userSession", userSession);
        return taskState.getTemplate();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTaskToTaskAndChangeTaskOrderInTaskstate(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        log.info("-------------------------------------------------------------------------------");
        log.info(" START: moveTaskToTask AndChangeTaskOrder In Taskstate ");
        log.info("        "+sourceTask.getTaskState().name());
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        boolean notEqualsId = ! sourceTask.equalsById(destinationTask);
        boolean notEquals = ! sourceTask.equalsByUniqueConstraint(destinationTask);
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameTaskType = sourceTask.hasSameTaskTypetAs(destinationTask);
        boolean go = notEqualsId && notEquals && sameContext && sameTaskType;
        if ( go ) {
            boolean srcIsBelowDestinationTask  = sourceTask.isBelowByTaskState(destinationTask);
            if (srcIsBelowDestinationTask) {
                this.taskService.moveTasksDownByTaskState( sourceTask, destinationTask );
            } else {
                this.taskService.moveTasksUpByTaskState( sourceTask, destinationTask );
            }
        }
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTaskToTask AndChangeTaskOrder In Taskstate ");
        log.info("        "+sourceTask.getTaskState().name());
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }

}