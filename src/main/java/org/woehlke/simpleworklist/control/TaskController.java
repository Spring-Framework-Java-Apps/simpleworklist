package org.woehlke.simpleworklist.control;

import java.util.ArrayList;
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
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
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
        List<TaskState> stateValues = new ArrayList<>();
        for(TaskState state: TaskState.values()){
            stateValues.add(state);
        }
        model.addAttribute("stateValues", TaskState.values());
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
            return "/task/detail/" + taskId;
        } else {
            persistentTask.setTitle(task.getTitle());
            persistentTask.setText(task.getText());
            persistentTask.setStatus(task.getStatus());
            persistentTask.setDueDate(task.getDueDate());
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
            thisProject = projectService.findByCategoryId(projectId);
            task.setProject(thisProject);
        }
        model.addAttribute("thisProject", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        List<TaskState> stateValues = new ArrayList<>();
        for(TaskState state: TaskState.values()){
            stateValues.add(state);
        }
        model.addAttribute("stateValues", TaskState.values());
        return "task/add";
    }

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.POST)
    public final String addNewTaskToProjectStore(
            @Valid Task task,
            @PathVariable long projectId,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
        }
        Project thisProject = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            task.setProject(null);
        } else {
            thisProject = projectService.findByCategoryId(projectId);
            task.setProject(thisProject);
        }
        task = taskService.saveAndFlush(task);
        LOGGER.info(task.toString());
        model.addAttribute("thisProject", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTask(@PathVariable long taskId) {
        Task task = taskService.findOne(taskId);
        long projectId = 0;
        if (task.getProject() != null) {
            projectId = task.getProject().getId();
        }
        taskService.delete(task);
        return "redirect:/project/" + projectId + "/";
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
        Project project = projectService.findByCategoryId(projectId);
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
        Project parentProject = projectService.findByCategoryId(projectId);
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
}
