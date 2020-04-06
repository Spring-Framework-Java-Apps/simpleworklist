package org.woehlke.simpleworklist.task;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

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

    @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskGet(
        @PathVariable("taskId") Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("editTaskGet");
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
        if(task != null) {
            Project thisProject = null;
            if (task.getProject() == null) {
                thisProject = new Project();
                thisProject.setId(0L);
            } else {
                thisProject = task.getProject();
            }
            model.addAttribute("thisProject", thisProject);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            model.addAttribute("areas", contexts);
            return "taskstate/task/edit";
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/edit", method = RequestMethod.POST)
    public final String editTaskPost(
        @PathVariable long taskId,
        @Valid Task task,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale,
        Model model
    ) {
        log.info("editTaskPost");
        Task persistentTask = taskService.findOne(taskId);
        long projectId = 0;
        Project thisProject;
        if (persistentTask.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = persistentTask.getProject();
            projectId = thisProject.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            model.addAttribute("thisProject", thisProject);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "taskstate/task/edit";
        } else {
            persistentTask.setTitle(task.getTitle());
            persistentTask.setText(task.getText());
            if(task.getDueDate()==null){
                persistentTask.setDueDate(null);
                if(persistentTask.getTaskState().compareTo(TaskState.SCHEDULED)==0){
                    persistentTask.setTaskState(TaskState.INBOX);
                }
            } else {
                persistentTask.setDueDate(task.getDueDate());
                persistentTask.setTaskState(TaskState.SCHEDULED);
            }
            persistentTask.setTaskTime(task.getTaskTime());
            persistentTask.setTaskEnergy(task.getTaskEnergy());
            boolean contextChanged =  persistentTask.getContext().equalsById(task.getContext());
            if(contextChanged){
                persistentTask.setContext(task.getContext());
                if(thisProject.getId()==0L) {
                    persistentTask.setRootProject();
                } else if(thisProject.getContext().equalsById(task.getContext())){
                    persistentTask.setProject(thisProject);
                }
                userSession.setContextId(task.getContext().getId());
                model.addAttribute("userSession", userSession);
                return "redirect:/project/root";
            }
            taskService.saveAndFlush(persistentTask);
            return "redirect:/project/" + projectId + "/";
        }
    }

    @RequestMapping(path = "/{taskId}/delete", method = RequestMethod.GET)
    public final String deleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("deleteTaskGet");
        if(task!= null){
            taskService.delete(task);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/{taskId}/undelete", method = RequestMethod.GET)
    public final String undeleteTaskGet(@PathVariable("taskId") Task task) {
        log.info("undeleteTaskGet");
        if(task!= null) {
            taskService.undelete(task);
            return "redirect:/taskstate/completed";
        } else {
            return "redirect:/taskstate/trash";
        }
    }

    @RequestMapping(path = "/{taskId}/transform", method = RequestMethod.GET)
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

    @RequestMapping(path = "/{taskId}/complete", method = RequestMethod.GET)
    public final String setDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task != null){
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.complete(task);
        }
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/incomplete/", method = RequestMethod.GET)
    public final String unsetDoneTaskGet(
        @PathVariable("taskId") Task task
    ) {
        if(task !=null) {
            taskService.incomplete(task);
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.saveAndFlush(task);
            return "redirect:/taskstate/"+task.getTaskState().name().toLowerCase();
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/setfocus/", method = RequestMethod.GET)
    public final String setFocusGet(
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back
    ){
        if(task !=null) {
            taskService.setFocus(task);
            return taskControllerService.getView(task,back);
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(path = "/{taskId}/unsetfocus", method = RequestMethod.GET)
    public final String unsetFocusGet(
        @PathVariable("taskId") Task task,
        @RequestParam(required=false) String back
    ){
        if(task !=null) {
            taskService.unsetFocus(task);
            return taskControllerService.getView(task,back);
        } else {
            return "redirect:/taskstate/inbox";
        }
    }
}
