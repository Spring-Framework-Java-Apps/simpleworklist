package org.woehlke.simpleworklist.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.project.ProjectService;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskService;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class TaskProjektServiceImpl implements TaskProjektService {

    private final ProjectService projectService;
    private final TaskService taskService;

    @Autowired
    public TaskProjektServiceImpl(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public String transformTaskIntoProjectGet(
        @NotNull Task task, @NotNull UserSessionBean userSession, @NotNull Model model
    ) {
        log.debug("transformTaskIntoProjectGet");
        Project thisProject = new Project();
        thisProject.setName(task.getTitle());
        thisProject.setDescription(task.getText());
        thisProject.setContext(task.getContext());
        if (task.getProject() != null) {
            long projectId = task.getProject().getId();
            Project parentProject = projectService.findByProjectId(projectId);
            thisProject.setParent(parentProject);
        }
        thisProject.setContext(task.getContext());
        thisProject = projectService.add(thisProject);
        task.setProject(null);
        task.moveToTrash();
        task.emptyTrash();
        task = taskService.updatedViaTaskstate(task);
        log.debug("tried to transform Task " + task.getId() + " to new Project " + thisProject.getId());
        model.addAttribute("userSession", userSession);
        return thisProject.getUrl();
    }
}
