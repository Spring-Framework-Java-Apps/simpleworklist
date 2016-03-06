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
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task != null) {
            Project thisProject = null;
            if (task.getProject() == null) {
                thisProject = new Project();
                thisProject.setId(0L);
                thisProject.setUserAccount(userAccount);
            } else {
                thisProject = task.getProject();
            }
            model.addAttribute("thisProject", thisProject);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "task/show";
        } else {
            return "redirect:/tasks/inbox";
        }
    }

    @RequestMapping(value = "/task/detail/{taskId}", method = RequestMethod.POST)
    public final String editTaskStore(
            @PathVariable long taskId,
            @Valid Task task,
            BindingResult result, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task persistentTask = taskService.findOne(taskId, userAccount);
        long projectId = 0;
        Project thisProject = null;
        if (persistentTask.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
        } else {
            thisProject = persistentTask.getProject();
            projectId = thisProject.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            model.addAttribute("thisProject", thisProject);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "task/show";
        } else {
            persistentTask.setTitle(task.getTitle());
            persistentTask.setText(task.getText());
            if(task.getDueDate()!=null){
                persistentTask.setDueDate(task.getDueDate());
                persistentTask.setTaskState(TaskState.SCHEDULED);
            }
            persistentTask.setLastChangeTimestamp(new Date());
            taskService.saveAndFlush(persistentTask, userAccount);
            return "redirect:/project/" + projectId + "/";
        }

    }

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.GET)
    public final String addNewTaskToProjectForm(
            @PathVariable long projectId,
            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setUserAccount(userAccount);
        task.setCreatedTimestamp(new Date());
        Project thisProject = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
        } else {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            task.setProject(thisProject);
        }
        model.addAttribute("thisProject", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "task/add";
    }

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.POST)
    public final String addNewTaskToProjectStore(
            @PathVariable long projectId,
            @Valid Task task,
            BindingResult result, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            return "task/add";
        } else {
            if (projectId == 0) {
                task.setProject(null);
            } else {
                Project thisProject = projectService.findByProjectId(projectId, userAccount);
                task.setProject(thisProject);
            }
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task = taskService.saveAndFlush(task, userAccount);
            LOGGER.info(task.toString());
            return "redirect:/project/" + projectId + "/";
        }
    }

    @RequestMapping(value = "/task/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTask(@PathVariable long taskId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!= null){
            taskService.delete(task, userAccount);
        }
        return "redirect:/tasks/trash";
    }

    @RequestMapping(value = "/task/undelete/{taskId}", method = RequestMethod.GET)
    public final String undeleteTask(@PathVariable long taskId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!= null) {
            taskService.undelete(task, userAccount);
            if (task.getProject() != null) {
                long projectId = task.getProject().getId();
                return "redirect:/project/" + projectId + "/";
            }
            switch (task.getTaskState()) {
                case SCHEDULED:
                    return "redirect:/tasks/scheduled";
                default:
                    return "redirect:/tasks/inbox";
            }
        } else {
            return "redirect:/tasks/inbox";
        }
    }

    @RequestMapping(value = "/task/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash() {
        UserAccount userAccount = userService.retrieveCurrentUser();
        taskService.emptyTrash(userAccount);
        return "redirect:/tasks/trash";
    }

    @RequestMapping(value = "/task/move/{taskId}", method = RequestMethod.GET)
    public final String moveTask(@PathVariable long taskId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        long projectId = 0;
        if (task != null) {
            if (task.getProject() != null) {
                projectId = task.getProject().getId();
            }
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/{taskId}/moveto/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(@PathVariable long taskId,
                                                 @PathVariable long projectId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!=null){
            Project project = projectService.findByProjectId(projectId, userAccount);
            task.setProject(project);
            taskService.saveAndFlush(task, userAccount);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/transform/{taskId}", method = RequestMethod.GET)
    public final String transformTaskIntoProject(@PathVariable long taskId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        long projectId = 0;
        if(task != null) {
            if (task.getProject() != null) {
                projectId = task.getProject().getId();
            }
            Project parentProject = projectService.findByProjectId(projectId, userAccount);
            Project thisProject = new Project();
            thisProject.setParent(parentProject);
            thisProject.setUserAccount(userAccount);
            thisProject.setName(task.getTitle());
            thisProject.setDescription(task.getText());
            thisProject.setUuid(task.getUuid());
            thisProject = projectService.saveAndFlush(thisProject, userAccount);
            taskService.delete(task, userAccount);
            projectId = thisProject.getId();
            LOGGER.info("tried to transform Task " + taskId + " to new Project " + projectId);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/task/complete/{taskId}", method = RequestMethod.GET)
    public final String completeTask(@PathVariable long taskId, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task != null){
            taskService.complete(task, userAccount);
        }
        return "redirect:/tasks/completed";
    }

    @RequestMapping(value = "/task/incomplete/{taskId}", method = RequestMethod.GET)
    public final String undoneTask(@PathVariable long taskId, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task !=null) {
            taskService.incomplete(task, userAccount);
            switch (task.getTaskState()) {
                case TODAY:
                    return "redirect:/tasks/today";
                case NEXT:
                    return "redirect:/tasks/next";
                case WAITING:
                    return "redirect:/tasks/waiting";
                case SCHEDULED:
                    return "redirect:/tasks/scheduled";
                case SOMEDAY:
                    return "redirect:/tasks/someday";
                default:
                    return "redirect:/tasks/inbox";
            }
        } else {
            return "redirect:/tasks/inbox";
        }
    }
}
