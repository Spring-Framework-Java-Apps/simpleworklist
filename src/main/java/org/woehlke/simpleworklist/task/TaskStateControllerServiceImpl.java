package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.application.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.application.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.project.ProjectService;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class TaskStateControllerServiceImpl implements TaskStateControllerService {

    private final BreadcrumbService breadcrumbService;
    private final TaskService taskService;
    private final ProjectService projectService;

    @Autowired
    public TaskStateControllerServiceImpl(
        BreadcrumbService breadcrumbService,
        TaskService taskService,
        ProjectService projectService)
    {
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
        this.projectService = projectService;
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
    public String transformTaskIntoProjectGet(@NotNull Task task) {
        Project thisProject = new Project();
        thisProject.setName(task.getTitle());
        thisProject.setDescription(task.getText());
        thisProject.setUuid(task.getUuid());
        thisProject.setContext(task.getContext());
        if (task.getProject() != null) {
            long projectId = task.getProject().getId();
            Project parentProject = projectService.findByProjectId(projectId);
            thisProject.setParent(parentProject);
        }
        thisProject = projectService.add(thisProject);
        task.setProject(null);
        task.moveToTrash();
        task.emptyTrash();
        taskService.updatedViaTaskstate(task);
        log.info("tried to transform Task " + task.getId() + " to new Project " + thisProject.getId());
        return thisProject.getUrl();
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
