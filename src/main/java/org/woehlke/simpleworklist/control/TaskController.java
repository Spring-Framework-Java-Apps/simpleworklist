package org.woehlke.simpleworklist.control;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.control.impl.AbstractController;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.enumerations.TaskEnergy;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskTime;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.TaskService;

@Controller
public class TaskController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/detail/{taskId}", method = RequestMethod.GET)
    public final String editTaskForm(@PathVariable long taskId, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
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
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            model.addAttribute("areas", contexts);
            return "t/task/show";
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
            return "t/task/show";
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

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.GET)
    public final String addNewTaskToProjectForm(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            BindingResult result,
            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setUserAccount(userAccount);
        task.setCreatedTimestamp(new Date());
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Project thisProject = null;
        Boolean mustChooseArea = false;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            if(userSession.getContextId() == 0L){
                mustChooseArea = true;
                task.setContext(userAccount.getDefaultContext());
                thisProject.setContext(userAccount.getDefaultContext());
            } else {
                Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                task.setContext(context);
                thisProject.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            task.setProject(thisProject);
            task.setContext(thisProject.getContext());
        }
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "t/task/add";
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

    @RequestMapping(value = "/task/addtoproject/{projectId}", method = RequestMethod.POST)
    public final String addNewTaskToProjectStore(
            @PathVariable long projectId,
            @ModelAttribute("userSession") UserSessionBean userSession,
            @Valid Task task,
            BindingResult result, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
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
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "t/task/add";
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
            return "redirect:/tasks/completed";
        } else {
            return "redirect:/tasks/trash";
        }
    }

    @RequestMapping(value = "/task/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
            @ModelAttribute("userSession") UserSessionBean userSession,Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
        taskService.emptyTrash(userAccount,context);
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
            long maxOrderIdProject = taskService.getMaxOrderIdProject(task.getProject(),task.getContext(),userAccount);
            task.setOrderIdProject(++maxOrderIdProject);
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
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(TaskState.COMPLETED,task.getContext(),userAccount);
            task.setOrderIdTaskState(++maxOrderIdTaskState);
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
            long maxOrderIdTaskState = taskService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext(),userAccount);
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            taskService.saveAndFlush(task,userAccount);
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
                case COMPLETED:
                    return "redirect:/tasks/completed";
                case TRASHED:
                    return "redirect:/tasks/trash";
                default:
                    return "redirect:/tasks/inbox";
            }
        } else {
            return "redirect:/tasks/inbox";
        }
    }

    @RequestMapping(value = "/task/setfocus/{taskId}", method = RequestMethod.GET)
    public final String setFocus(@PathVariable long taskId,
                                 @RequestParam(required=false) String back,
                                 Model model){
        UserAccount userAccount = userService.retrieveCurrentUser();
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
                    return "redirect:/tasks/today";
                case NEXT:
                    return "redirect:/tasks/next";
                case WAITING:
                    return "redirect:/tasks/waiting";
                case SCHEDULED:
                    return "redirect:/tasks/scheduled";
                case SOMEDAY:
                    return "redirect:/tasks/someday";
                case COMPLETED:
                    return "redirect:/tasks/completed";
                default:
                    return "redirect:/tasks/inbox";
            }
        } else {
            return "redirect:/tasks/inbox";
        }
    }

    @RequestMapping(value = "/task/unsetfocus/{taskId}", method = RequestMethod.GET)
    public final String unsetFocus(@PathVariable long taskId,
                                   @RequestParam(required=false) String back,
                                   Model model){
        UserAccount userAccount = userService.retrieveCurrentUser();
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
                    return "redirect:/tasks/today";
                case NEXT:
                    return "redirect:/tasks/next";
                case WAITING:
                    return "redirect:/tasks/waiting";
                case SCHEDULED:
                    return "redirect:/tasks/scheduled";
                case SOMEDAY:
                    return "redirect:/tasks/someday";
                case COMPLETED:
                    return "redirect:/tasks/completed";
                default:
                    return "redirect:/tasks/inbox";
            }
        } else {
            return "redirect:/tasks/inbox";
        }
    }

    @RequestMapping(value = "/tasks/all", method = RequestMethod.GET)
    public String getAllTasksForUser(
            @RequestParam(defaultValue = "changed", required = false) String sort,
            @RequestParam(defaultValue = "desc", required = false) String sortDir,
            @RequestParam(defaultValue = "1", required = false) int page,
            Locale locale,
            Model model){
        Sort.Direction sortDirection = Sort.Direction.DESC;
        if(sortDir.compareTo("asc") == 0){
            sortDirection = Sort.Direction.ASC;
        }
        Pageable request = new PageRequest(page - 1, pageSize, sortDirection, "lastChangeTimestamp");
        switch (sort){
            case "title":
                request = new PageRequest(page - 1, pageSize, sortDirection, "title", "lastChangeTimestamp");
                break;
            case "text":
                request = new PageRequest(page - 1, pageSize, sortDirection, "text", "lastChangeTimestamp");
                break;
            case "duedate":
                request = new PageRequest(page - 1, pageSize, sortDirection, "dueDate", "lastChangeTimestamp");
                break;
            case "state":
                request = new PageRequest(page - 1, pageSize, sortDirection, "taskState", "lastChangeTimestamp");
                break;
            case "project":
                request = new PageRequest(page - 1, pageSize, sortDirection, "project.name", "lastChangeTimestamp");
                break;
            case "context":
                if(locale.getLanguage().toLowerCase().compareTo("de")==0){
                    request = new PageRequest(page - 1, pageSize, sortDirection, "context.nameDe", "lastChangeTimestamp");
                } else {
                    request = new PageRequest(page - 1, pageSize, sortDirection, "context.nameEn", "lastChangeTimestamp");
                }
                break;
            default:
                break;
        }
        UserAccount userAccount = userService.retrieveCurrentUser();
        Page<Task> taskPage = taskService.findByUser(userAccount,request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("sort",sort);
        model.addAttribute("sortDir",sortDir);
        return "t/tasks/all";
    }

    @RequestMapping(value = "/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
            @PathVariable long sourceTaskId,
            @PathVariable long destinationTaskId,
            Model model){
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task sourceTask = taskService.findOne(sourceTaskId,userAccount);
        Task destinationTask = taskService.findOne(destinationTaskId,userAccount);
        LOGGER.info("------------- changeTaskOrderId -------------");
        LOGGER.info("source Task:      "+sourceTask.toString());
        LOGGER.info("---------------------------------------------");
        LOGGER.info("destination Task: "+destinationTask.toString());
        LOGGER.info("---------------------------------------------");
        String returnUrl = "redirect:/tasks/inbox";
        if(sourceTask.getUserAccount().getId().longValue()==destinationTask.getUserAccount().getId().longValue()) {
            boolean sameTaskType = (sourceTask.getTaskState().ordinal() == destinationTask.getTaskState().ordinal());
            if (sameTaskType) {
                taskService.moveOrderIdTaskState(sourceTask, destinationTask);
                returnUrl = "redirect:/tasks/" + sourceTask.getTaskState().name().toLowerCase();
            }
        }
        return returnUrl;
    }

    @RequestMapping(value = "/project/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderIdByProject(
            @PathVariable long sourceTaskId,
            @PathVariable long destinationTaskId,
            Model model){
        UserAccount userAccount = userService.retrieveCurrentUser();
        Task sourceTask = taskService.findOne(sourceTaskId,userAccount);
        Task destinationTask = taskService.findOne(destinationTaskId,userAccount);
        LOGGER.info("--------- changeTaskOrderIdByProject  -------");
        LOGGER.info("source Task:      "+sourceTask.toString());
        LOGGER.info("---------------------------------------------");
        LOGGER.info("destination Task: "+destinationTask.toString());
        LOGGER.info("---------------------------------------------");
        String returnUrl = "redirect:/tasks/inbox";
        if(sourceTask.getUserAccount().getId().longValue()==destinationTask.getUserAccount().getId().longValue()){
            if(sourceTask.getProject() == null && destinationTask.getProject() == null) {
                taskService.moveOrderIdProject(sourceTask,destinationTask);
                returnUrl = "redirect:/project/0";
            } else if (sourceTask.getProject() != null && destinationTask.getProject() != null) {
                boolean sameProject = (sourceTask.getProject().getId().longValue() == destinationTask.getProject().getId().longValue());
                if (sameProject) {
                    taskService.moveOrderIdProject(sourceTask,destinationTask);
                    returnUrl = "redirect:/project/" + sourceTask.getProject().getId();
                }
            }
        }
        return returnUrl;
    }
}
