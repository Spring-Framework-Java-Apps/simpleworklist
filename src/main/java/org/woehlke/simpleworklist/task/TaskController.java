package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.project.Project;

@Slf4j
@Controller
@RequestMapping(path = "/task")
public class TaskController extends AbstractController {

    @RequestMapping(path = "/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("deleteTaskGet");
        if(task!= null){
            taskService.delete(task);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/task/undelete/{taskId}", method = RequestMethod.GET)
    public final String undeleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("undeleteTaskGet");
        if(task!= null) {
            taskService.undelete(task);
            return "redirect:/taskstate/completed";
        } else {
            return "redirect:/taskstate/trash";
        }
    }

    @RequestMapping(path = "/transform/{taskId}", method = RequestMethod.GET)
    public final String transformTaskIntoProjectGet(@PathVariable("taskId") Task task) {
        log.info("transformTaskIntoProjectGet");
        if(task != null) {
            long projectId = 0;
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
            if(projectId == 0){
                return "redirect:/project/root/";
            } else {
                return "redirect:/project/" + projectId + "/";
            }
        }
        return "redirect:/taskstate/inbox";
    }

}
