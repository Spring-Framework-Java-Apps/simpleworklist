package org.woehlke.simpleworklist.control;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TaskService;

@Controller
public class TaskController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Inject
    private TaskService taskService;

    @RequestMapping(value = "/task/detail/{taskId}", method = RequestMethod.GET)
    public final String editTaskForm(@PathVariable long taskId, Model model) {
        Task task = taskService.findOne(taskId);
        Project thisProject = null;
        if (task.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = task.getProject();
        }
        model.addAttribute("thisProject", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "task/show";
    }

    @RequestMapping(value = "/task/detail/{taskId}", method = RequestMethod.POST)
    public final String editTaskStore(
            @PathVariable long taskId,
            @Valid Task task,
            BindingResult result, Model model) {
        Task persistentTask = taskService.findOne(taskId);
        long projectId = 0;
        Project thisProject = null;
        if (persistentTask.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = persistentTask.getProject();
            projectId = thisProject.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            model.addAttribute("thisProject", thisProject);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "task/show";
        } else {
            persistentTask.setTitle(task.getTitle());
            persistentTask.setText(task.getText());
            if(task.getDueDate()!=null){
                persistentTask.setDueDate(task.getDueDate());
                persistentTask.setFocusType(FocusType.SCHEDULED);
            }
            persistentTask.setLastChangeTimestamp(new Date());
            taskService.saveAndFlush(persistentTask);
            return "redirect:/project/" + projectId + "/";
        }

    }

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.GET)
    public final String addNewTaskToProjectForm(
            @PathVariable long projectId,
            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = new Task();
        task.setFocusType(FocusType.INBOX);
        task.setUserAccount(userAccount);
        task.setCreatedTimestamp(new Date());
        Project thisProject = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = projectService.findByProjectId(projectId);
            task.setProject(thisProject);
        }
        model.addAttribute("thisProject", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "task/add";
    }

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.POST)
    public final String addNewTaskToProjectStore(
            @PathVariable long projectId,
            @Valid Task task,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            return "task/add";
        } else {
            if (projectId == 0) {
                task.setProject(null);
            } else {
                Project thisProject = projectService.findByProjectId(projectId);
                task.setProject(thisProject);
            }
            if(task.getDueDate()==null){
                task.setFocusType(FocusType.INBOX);
            } else {
                task.setFocusType(FocusType.SCHEDULED);
            }
            task = taskService.saveAndFlush(task);
            LOGGER.info(task.toString());
            return "redirect:/project/" + projectId + "/";
        }
    }

    @RequestMapping(value = "/task/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTask(@PathVariable long taskId) {
        Task task = taskService.findOne(taskId);
        taskService.delete(task);
        return "redirect:/focus/trash";
    }

    @RequestMapping(value = "/task/undelete/{taskId}", method = RequestMethod.GET)
    public final String undeleteTask(@PathVariable long taskId) {
        Task task = taskService.findOne(taskId);
        taskService.undelete(task);
        if (task.getProject() != null) {
            long projectId = task.getProject().getId();
            return "redirect:/project/" + projectId + "/";
        }
        switch (task.getFocusType()){
            case SCHEDULED: return "redirect:/focus/scheduled";
            default: return "redirect:/focus/inbox";
        }
    }

    @RequestMapping(value = "/task/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash() {
        UserAccount userAccount = userService.retrieveCurrentUser();
        taskService.emptyTrash(userAccount);
        return "redirect:/focus/trash";
    }

    @RequestMapping(value = "/task/move/{taskId}", method = RequestMethod.GET)
    public final String moveTask(@PathVariable long taskId) {
        Task task = taskService.findOne(taskId);
        long projectId = 0;
        if (task.getProject() != null) {
            projectId = task.getProject().getId();
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/{taskId}/moveto/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(@PathVariable long taskId,
                                                 @PathVariable long projectId) {
        Task task = taskService.findOne(taskId);
        Project project = projectService.findByProjectId(projectId);
        task.setProject(project);
        taskService.saveAndFlush(task);
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/transform/{taskId}", method = RequestMethod.GET)
    public final String transformTaskIntoProject(@PathVariable long taskId) {
        Task task = taskService.findOne(taskId);
        long projectId = 0;
        if (task.getProject() != null) {
            projectId = task.getProject().getId();
        }
        Project parentProject = projectService.findByProjectId(projectId);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Project thisProject = new Project();
        thisProject.setParent(parentProject);
        thisProject.setUserAccount(userAccount);
        thisProject.setName(task.getTitle());
        thisProject.setDescription(task.getText());
        thisProject.setUuid(task.getUuid());
        thisProject = projectService.saveAndFlush(thisProject);
        taskService.delete(task);
        projectId = thisProject.getId();
        LOGGER.info("tried to transform Task "+taskId+" to new Project "+projectId);
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/complete/{taskId}", method = RequestMethod.GET)
    public final String completeTask(@PathVariable long taskId, Model model) {
        Task task = taskService.findOne(taskId);
        taskService.complete(task);
        return "redirect:/focus/completed";
    }

    @RequestMapping(value = "/task/incomplete/{taskId}", method = RequestMethod.GET)
    public final String undoneTask(@PathVariable long taskId, Model model) {
        Task task = taskService.findOne(taskId);
        taskService.incomplete(task);
        switch (task.getFocusType()){
            case TODAY: return "redirect:/focus/today";
            case NEXT: return "redirect:/focus/next";
            case WAITING: return "redirect:/focus/waiting";
            case SCHEDULED: return "redirect:/focus/scheduled";
            case SOMEDAY: return "redirect:/focus/someday";
            default: return "redirect:/focus/inbox";
        }
    }
}
