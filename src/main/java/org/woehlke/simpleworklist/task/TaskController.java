package org.woehlke.simpleworklist.task;

import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;

@Slf4j
@Controller
@RequestMapping(path = "/task")
public class TaskController extends AbstractController {

    private final TaskMoveService taskMoveService;
    private final TaskControllerService taskControllerService;

    @Autowired
    public TaskController(TaskMoveService taskMoveService, TaskControllerService taskControllerService) {
        this.taskMoveService = taskMoveService;
        this.taskControllerService = taskControllerService;
    }


    @RequestMapping(path = "/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTaskGet(@PathVariable("taskId") Task task) {
        if(task!= null){
            taskService.delete(task);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/task/undelete/{taskId}", method = RequestMethod.GET)
    public final String undeleteTaskGet(@PathVariable("taskId") Task task) {
        if(task!= null) {
            taskService.undelete(task);
            return "redirect:/taskstate/completed";
        } else {
            return "redirect:/taskstate/trash";
        }
    }

    @RequestMapping(path = "/transform/{taskId}", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(@PathVariable("taskId") Task task) {
        long projectId = 0;
        if(task != null) {
            if (task.getProject() != null) {
                projectId = task.getProject().getId();
            }
            Project parentProject = projectService.findByProjectId(projectId);
            Project thisProject = new Project();
            thisProject.setParent(parentProject);
            thisProject.setName(task.getTitle());
            thisProject.setDescription(task.getText());
            thisProject.setUuid(task.getUuid());
            thisProject.setContext(task.getContext());
            thisProject = projectService.saveAndFlush(thisProject);
            taskService.delete(task);
            projectId = thisProject.getId();
            log.info("tried to transform Task " + task.getId() + " to new Project " + projectId);
        }
        return "redirect:/project/" + projectId + "/";
    }

}
