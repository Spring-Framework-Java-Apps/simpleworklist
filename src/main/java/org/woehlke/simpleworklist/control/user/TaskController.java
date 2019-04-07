package org.woehlke.simpleworklist.control.user;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.common.AbstractController;
import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.Task;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.entities.Project;
import org.woehlke.simpleworklist.entities.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;
import org.woehlke.simpleworklist.model.beans.UserSessionBean;
import org.woehlke.simpleworklist.entities.services.TaskService;

@Controller
@RequestMapping(value = "/task")
public class TaskController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/{taskId}/edit", method = RequestMethod.GET)
    public final String editTaskForm(@PathVariable long taskId, Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        List<Context> contexts = contextService.getAllForUser(userAccount);
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            model.addAttribute("areas", contexts);
            return "task/edit";
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(value = "/{taskId}/edit", method = RequestMethod.POST)
    public final String editTaskStore(
            @PathVariable long taskId,
            @Valid Task task,
            BindingResult result, Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "task/edit";
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
            persistentTask.setLastChangeTimestamp(new Date());
            boolean contextChanged =  persistentTask.getContext().getId().longValue() != task.getContext().getId().longValue();
            if(contextChanged){
                persistentTask.setContext(task.getContext());
                persistentTask.setProject(null);
                model.addAttribute("userSession", new UserSessionBean(task.getContext().getId()));
                return "redirect:/project/0/";
            }
            taskService.saveAndFlush(persistentTask, userAccount);
            return "redirect:/project/" + projectId + "/";
        }

    }

    private Project getProject(long projectId, UserAccount userAccount, UserSessionBean userSession){
        Project thisProject = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            if(userSession.getContextId() == 0L){
                thisProject.setContext(userAccount.getDefaultContext());
            } else {
                Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                thisProject.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId, userAccount);
        }
        return thisProject;
    }

    @RequestMapping(value = "/addtoproject/{projectId}", method = RequestMethod.POST)
    public final String addNewTaskToProjectStore(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            @Valid Task task,
            BindingResult result, Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            Project thisProject = this.getProject(projectId, userAccount, userSession);
            Boolean mustChooseArea = false;
            if (projectId == 0) {
                if(userSession.getContextId() == 0L){
                    mustChooseArea = true;
                } else {
                    Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                    task.setContext(context);
                }
            } else {
                task.setProject(thisProject);
                task.setContext(thisProject.getContext());
            }
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject, userAccount);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "task/add";
        } else {
            if (projectId == 0) {
                task.setProject(null);
            } else {
                Project thisProject = projectService.findByProjectId(projectId, userAccount);
                task.setProject(thisProject);
                task.setContext(thisProject.getContext());
            }
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task.setFocus(false);
            Context context = contextService.findByIdAndUserAccount(task.getContext().getId(), userAccount);
            task.setContext(context);
            long maxOrderIdProject = taskService.getMaxOrderIdProject(task.getProject(),context,userAccount);
            task.setOrderIdProject(++maxOrderIdProject);
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext(),userAccount);
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.saveAndFlush(task, userAccount);
            LOGGER.info(task.toString());
            return "redirect:/project/" + projectId + "/";
        }
    }

    @RequestMapping(value = "/delete/{taskId}", method = RequestMethod.GET)
    public final String deleteTask(@PathVariable long taskId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!= null){
            taskService.delete(task, userAccount);
        }
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(value = "/task/undelete/{taskId}", method = RequestMethod.GET)
    public final String undeleteTask(@PathVariable long taskId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task!= null) {
            taskService.undelete(task, userAccount);
            return "redirect:/taskstate/completed";
        } else {
            return "redirect:/taskstate/trash";
        }
    }


    //TODO: what is this for?
    @RequestMapping(value = "/task/move/{taskId}", method = RequestMethod.GET)
    public final String moveTask(@PathVariable long taskId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        long projectId = 0;
        if (task != null) {
            if (task.getProject() != null) {
                projectId = task.getProject().getId();
            }
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/transform/{taskId}", method = RequestMethod.GET)
    public final String transformTaskIntoProject(@PathVariable long taskId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
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
            thisProject.setContext(task.getContext());
            thisProject = projectService.saveAndFlush(thisProject, userAccount);
            taskService.delete(task, userAccount);
            projectId = thisProject.getId();
            LOGGER.info("tried to transform Task " + taskId + " to new Project " + projectId);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(value = "/complete/{taskId}", method = RequestMethod.GET)
    public final String completeTask(@PathVariable long taskId, Model model) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task != null){
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext(),userAccount);
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.complete(task, userAccount);
        }
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(value = "/incomplete/{taskId}", method = RequestMethod.GET)
    public final String undoneTask(@PathVariable long taskId) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task !=null) {
            taskService.incomplete(task, userAccount);
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext(),userAccount);
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.saveAndFlush(task,userAccount);
            switch (task.getTaskState()) {
                case TODAY:
                    return "redirect:/taskstate/today";
                case NEXT:
                    return "redirect:/taskstate/next";
                case WAITING:
                    return "redirect:/taskstate/waiting";
                case SCHEDULED:
                    return "redirect:/taskstate/scheduled";
                case SOMEDAY:
                    return "redirect:/taskstate/someday";
                case COMPLETED:
                    return "redirect:/taskstate/completed";
                case TRASH:
                    return "redirect:/taskstate/trash";
                default:
                    return "redirect:/taskstate/inbox";
            }
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(value = "/setfocus/{taskId}", method = RequestMethod.GET)
    public final String setFocus(@PathVariable long taskId,
                                 @RequestParam(required=false) String back){
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task !=null) {
            taskService.setFocus(task, userAccount);
            if(back != null && back.contentEquals("project")){
                if(task.getProject() != null) {
                    return "redirect:/project/" + task.getProject().getId();
                } else {
                    return "redirect:/project/0";
                }
            }
            switch (task.getTaskState()) {
                case TODAY:
                    return "redirect:/taskstate/today";
                case NEXT:
                    return "redirect:/taskstate/next";
                case WAITING:
                    return "redirect:/taskstate/waiting";
                case SCHEDULED:
                    return "redirect:/taskstate/scheduled";
                case SOMEDAY:
                    return "redirect:/taskstate/someday";
                case COMPLETED:
                    return "redirect:/taskstate/completed";
                default:
                    return "redirect:/taskstate/inbox";
            }
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

    @RequestMapping(value = "/unsetfocus/{taskId}", method = RequestMethod.GET)
    public final String unsetFocus(@PathVariable long taskId,
                                   @RequestParam(required=false) String back){
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, userAccount);
        if(task !=null) {
            taskService.unsetFocus(task, userAccount);
            if(back != null && back.contentEquals("project")){
                if(task.getProject() != null) {
                    return "redirect:/project/" + task.getProject().getId();
                } else {
                    return "redirect:/project/0";
                }
            }
            switch (task.getTaskState()) {
                case TODAY:
                    return "redirect:/taskstate/today";
                case NEXT:
                    return "redirect:/taskstate/next";
                case WAITING:
                    return "redirect:/taskstate/waiting";
                case SCHEDULED:
                    return "redirect:/taskstate/scheduled";
                case SOMEDAY:
                    return "redirect:/taskstate/someday";
                case COMPLETED:
                    return "redirect:/taskstate/completed";
                default:
                    return "redirect:/taskstate/inbox";
            }
        } else {
            return "redirect:/taskstate/inbox";
        }
    }

}
