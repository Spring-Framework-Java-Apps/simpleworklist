package org.woehlke.simpleworklist.taskstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskEnergy;
import org.woehlke.simpleworklist.task.TaskTime;
import org.woehlke.simpleworklist.user.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by tw on 21.02.16.
 */
@Slf4j
@Controller
@RequestMapping(path = "/taskstate/task")
public class TaskStateMoveController extends AbstractController {

    private final TaskMoveService taskMoveService;

    @Autowired
    public TaskStateMoveController(
        TaskMoveService taskMoveService
    ) {
        this.taskMoveService = taskMoveService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public final String addNewTaskToInboxGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("addNewTaskToInboxGet");
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Boolean mustChooseContext = false;
        if(userSession.getContextId() == 0L){
            mustChooseContext = true;
            task.setContext(userAccount.getDefaultContext());
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
            task.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseContext);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "taskstate/task/add";
    }

    @RequestMapping(path = "/add",  method = RequestMethod.POST)
    public final String addNewTaskToInboxPost(
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result,
        Locale locale,
        Model model
    ) {
        log.info("addNewTaskToInboxPost");
        Context context = super.getContext(userSession);
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Boolean mustChooseArea = false;
            task.setContext(context);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForTaskstate(TaskState.INBOX,locale);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            return "taskstate/task/add";
        } else {
            task = taskService.addToInbox(task);
            log.info(task.toString());
            return "redirect:/taskstate/" + task.getTaskState().name().toLowerCase();
        }
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
            Context thisContext = task.getContext();
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("thisContext", thisContext);
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
        if (result.hasErrors() || taskId != task.getId()) {
            if(result.hasErrors()) {
                log.warn("result.hasErrors");
                for (ObjectError e : result.getAllErrors()) {
                    log.error(e.toString());
                }
            }
            if (taskId != task.getId()) {
                log.error("taskId "+taskId+" != task.getId "+task.getId());
            }
            Task persistentTask = taskService.findOne(taskId);
            if(task.getId()!= persistentTask.getId()){
                log.error("task.getId()!= persistentTask.getId()");
            }
            if(persistentTask.isInRootProject()) {
                Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
                model.addAttribute("breadcrumb", breadcrumb);
                task.setRootProject();
            } else {
                Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(persistentTask.getProject(),locale);
                model.addAttribute("breadcrumb", breadcrumb);
                task.setProject(persistentTask.getProject());
            }
            model.addAttribute("task", task);
            return "taskstate/task/edit";
        } else {
            /*
            Task persistentTask = taskService.findOne(taskId);
            persistentTask.updateTo(task);
            persistentTask.switchTaskState(task.getTaskState(), task.getContext(), task.getDueDate(), thisProject);            if(task.getDueDate()==null){
                persistentTask.setDueDate(null);
                if(persistentTask.getTaskState().compareTo(TaskState.SCHEDULED)==0){
                    persistentTask.setTaskState(task.getTaskState());
                }
            } else {
                persistentTask.setDueDate(task.getDueDate());
                persistentTask.setTaskState(TaskState.SCHEDULED);
            }
            boolean contextChanged = persistentTask.getContext().equalsById();
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
            */
            task.unsetFocus();
            task.setRootProject();
            Task persistentTask = taskService.findOne(task.getId());
            persistentTask.merge(task);
            task = taskService.updatedViaTaskstate(persistentTask);
            return "redirect:" + task.getTaskState().getUrl();
        }
    }

    @RequestMapping(path = "/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderId(
        @PathVariable("sourceTaskId") Task sourceTask,
        @PathVariable("destinationTaskId") Task destinationTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        userSession.setLastTaskState(sourceTask.getTaskState());
        model.addAttribute("userSession", userSession);
        log.info("------------- changeTaskOrderId -------------");
        log.info("source Task:      "+sourceTask.toString());
        log.info("---------------------------------------------");
        log.info("destination Task: "+destinationTask.toString());
        log.info("---------------------------------------------");
        taskMoveService.moveOrderIdTaskState(sourceTask, destinationTask);
        return "redirect:/taskstate/" + sourceTask.getTaskState().name().toLowerCase();
    }

    @RequestMapping(path = "/{taskId}/move/to/project/{projectId}", method = RequestMethod.GET)
    public final String moveTaskToAnotherProject(
        @PathVariable("taskId") Task task,
        @PathVariable long projectId
    ) {
        if(projectId == 0) {
            task = taskMoveService.moveTaskToRootProject(task);
        } else {
            Project project = projectService.findByProjectId(projectId);
            task = taskMoveService.moveTaskToAnotherProject(task,project);
        }
        return "redirect:/project/" + projectId + "/";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to inbox");
        task.moveToInbox();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/inbox";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to today");
        task.moveToToday();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/today";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to next");
        task.moveToNext();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/next";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to waiting");
        task.moveToWaiting();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/waiting";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to someday");
        task.moveToSomeday();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/someday";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/focus", method = RequestMethod.GET)
    public final String moveTaskToFocus(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to focus");
        task.moveToFocus();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/focus";
    }

    @RequestMapping(path = "/{taskId}/move/to/taskstate/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to completed");
        task.moveToCompletedTasks();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/completed";
    }

    @RequestMapping(path = "/{taskId}/move/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable("taskId") Task task) {
        log.info("dragged and dropped "+task.getId()+" to trash");
        task.moveToTrash();
        taskService.updatedViaTaskstate(task);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/completed/move/to/trash", method = RequestMethod.GET)
    public final String moveAllCompletedToTrash(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskMoveService.moveAllCompletedToTrash(context);
        return "redirect:/taskstate/trash";
    }

    @RequestMapping(path = "/trash/empty", method = RequestMethod.GET)
    public final String emptyTrash(
        @ModelAttribute("userSession") UserSessionBean userSession
    ) {
        Context context = super.getContext(userSession);
        taskMoveService.emptyTrash(context);
        return "redirect:/taskstate/trash";
    }

}
