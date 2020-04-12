package org.woehlke.simpleworklist.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.project.ProjectService;

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
    public String transformTaskIntoProjectGet(@NotNull Task task) {
        log.info("transformTaskIntoProjectGet");
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
}
