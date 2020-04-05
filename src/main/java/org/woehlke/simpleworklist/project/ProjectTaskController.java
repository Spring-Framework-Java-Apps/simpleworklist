package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskEnergy;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.task.TaskTime;
import org.woehlke.simpleworklist.taskstate.TaskMoveService;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.Valid;
import java.util.Locale;


@Slf4j
@Controller
@RequestMapping(path = "/project")
public class ProjectTaskController extends AbstractController {

    private final TaskMoveService taskMoveService;
    private final ProjectControllerService projectControllerService;

    @Autowired
    public ProjectTaskController(TaskMoveService taskMoveService, ProjectControllerService projectControllerService) {
        this.taskMoveService = taskMoveService;
        this.projectControllerService = projectControllerService;
    }

    @RequestMapping(path = "/{projectId}/task/{sourceTaskId}/changeorderto/{destinationTaskId}", method = RequestMethod.GET)
    public String changeTaskOrderIdWithinAProject(
        @PathVariable("projectId") Project thisProject,
        @PathVariable("sourceTaskId") Task sourceTask,
        @PathVariable("destinationTaskId") Task destinationTask,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Model model
    ){
        if(!sourceTask.isInRootProject()){
            userSession.setLastProjectId(sourceTask.getProject().getId());
        }
        model.addAttribute("userSession",userSession);
        log.info("-------------------------------------------------");
        log.info("  changeTaskOrderIdWithinAProject");
        log.info("-------------------------------------------------");
        log.info("  source Task:      "+sourceTask.toString());
        log.info("-------------------------------------------------");
        log.info("  destination Task: "+destinationTask.toString());
        log.info("-------------------------------------------------");
        String returnUrl = "redirect:/taskstate/inbox";
        boolean rootProject = sourceTask.isInRootProject();
        returnUrl = "redirect:/project/0";
        if(rootProject){
            taskMoveService.moveOrderIdRootProject(sourceTask, destinationTask);
        } else {
            taskMoveService.moveOrderIdProject(sourceTask, destinationTask);
            log.info("  DONE: taskMoveService.moveOrderIdProject (2)");
            returnUrl = "redirect:/project/" + sourceTask.getProject().getId();
        }
        log.info("-------------------------------------------------");
        return returnUrl;
    }

    @RequestMapping(path = "/{projectId}/task/add/", method = RequestMethod.GET)
    public final String addNewTaskToProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        UserAccount userAccount = userAccountLoginSuccessService.retrieveCurrentUser();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Boolean mustChooseArea = false;
        if(userSession.getContextId() == 0L){
            mustChooseArea = true;
            task.setContext(userAccount.getDefaultContext());
        } else {
            Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
            task.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProjectId", 0L);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "id/task/add";
    }

    @RequestMapping(path = "/{projectId}/task/add/", method = RequestMethod.POST)
    public final String addNewTaskToProjectPost(
        @PathVariable long projectId,
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result, Locale locale, Model model) {
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                log.info(e.toString());
            }
            Project thisProject = projectControllerService.getProject(projectId, userAccount, userSession);
            Boolean mustChooseArea = false;
                task.setProject(thisProject);
                task.setContext(thisProject.getContext());
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("mustChooseArea", mustChooseArea);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("task", task);
            //return "task/addToProject";
            return "id/task/add";
        } else {
                Project thisProject = projectService.findByProjectId(projectId);
                task.setProject(thisProject);
                task.setContext(thisProject.getContext());
            if(task.getDueDate()==null){
                task.setTaskState(TaskState.INBOX);
            } else {
                task.setTaskState(TaskState.SCHEDULED);
            }
            task.setFocus(false);
            task.setContext(context);
            long maxOrderIdProject = taskMoveService.getMaxOrderIdProject(task.getProject(),context);
            task.setOrderIdProject(++maxOrderIdProject);
            long maxOrderIdTaskState = taskMoveService.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
            task.setOrderIdTaskState(++maxOrderIdTaskState);
            task = taskService.saveAndFlush(task);
            log.info(task.toString());
            return "redirect:/project/" + projectId + "/";
        }
    }

}
