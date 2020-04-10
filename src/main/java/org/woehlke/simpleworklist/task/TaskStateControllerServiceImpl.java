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
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.project.ProjectService;
import org.woehlke.simpleworklist.session.UserSessionBean;

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
        ProjectService projectService) {
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
        this.projectService = projectService;
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
        return taskState.getTemplate();
    }

    @Override
    public String transformTaskIntoProjectGet(Task task) {
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
        thisProject = projectService.saveAndFlush(thisProject);
        task.setProject(null);
        task.moveToTrash();
        task.emptyTrash();
        taskService.updatedViaTaskstate(task);
        log.info("tried to transform Task " + task.getId() + " to new Project " + thisProject.getId());
        return thisProject.getUrl();
    }
}
