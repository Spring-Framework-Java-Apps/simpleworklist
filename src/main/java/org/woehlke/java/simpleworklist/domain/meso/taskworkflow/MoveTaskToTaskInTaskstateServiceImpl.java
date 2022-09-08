package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.Locale;

@Slf4j
@Service
public class MoveTaskToTaskInTaskstateServiceImpl implements MoveTaskToTaskInTaskstateService {

    private final BreadcrumbService breadcrumbService;
    private final TaskService taskService;

    @Autowired
    public MoveTaskToTaskInTaskstateServiceImpl(
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(taskState,locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        model.addAttribute("taskstateType", taskState.getType() );
        model.addAttribute("userSession", userSession);
        model.addAttribute("dataPage", true);
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
