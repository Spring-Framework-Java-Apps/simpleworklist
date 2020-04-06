package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
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
public class ProjectRootController extends AbstractController {

    private final ProjectControllerService projectControllerService;
    private final TaskMoveService taskMoveService;

    @Autowired
    public ProjectRootController(
        ProjectControllerService projectControllerService,
        TaskMoveService taskMoveService
    ) {
        this.projectControllerService = projectControllerService;
        this.taskMoveService = taskMoveService;
    }

    @RequestMapping(path="/root", method = RequestMethod.GET)
    public final String showRootProject(
        @PageableDefault(sort = "orderIdProject") Pageable pageable,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) boolean isDeleted,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("/project/root");
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(0L);
        model.addAttribute("userSession",userSession);
        Page<Task> taskPage = taskService.findByRootProject(context,pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
            model.addAttribute("myTaskState","PROJECT");
        }
        return "project/root/show";
    }

    @RequestMapping(path = "/root/add/project", method = RequestMethod.GET)
    public final String addNewTopLevelProjectForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ){
        log.info("/project/root/add/project (GET)");
        Context context = super.getContext(userSession);
        projectControllerService.addNewProjectToRoot(userSession, context, locale, model);
        return "project/root/add/project";
    }

    @RequestMapping(path = "/root/add/project", method = RequestMethod.POST)
    public final String addNewTopLevelProjectSave(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ){
        log.info("/project/root/add/project (POST)");
        Context context = super.getContext(userSession);
        return projectControllerService.addNewProjectToRootPersist(
            userSession,
            project,
            context,
            result,
            locale,
            model,
            "project/root/add/project"
        );
    }

    @RequestMapping(path = "/root/add/task", method = RequestMethod.GET)
    public final String addNewTaskToRootProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("/project/root/add/task (GET)");
        Context context = super.getContext(userSession);
        UserAccount userAccount = context.getUserAccount();
        Task task = new Task();
        task.setTaskState(TaskState.INBOX);
        task.setTaskEnergy(TaskEnergy.NONE);
        task.setTaskTime(TaskTime.NONE);
        Project thisProject;
        Boolean mustChooseArea = false;
        thisProject = new Project();
        thisProject.setId(0L);
        if(userSession.getContextId() == 0L){
            mustChooseArea = true;
            task.setContext(userAccount.getDefaultContext());
            thisProject.setContext(userAccount.getDefaultContext());
        } else {
            task.setContext(context);
            thisProject.setContext(context);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("mustChooseArea", mustChooseArea);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("thisProjectId", thisProject.getId());
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("task", task);
        return "project/root/add/task";
    }

    @RequestMapping(path = "/root/add/task", method = RequestMethod.POST)
    public final String addNewTaskToRootProjectPost(
        @ModelAttribute("userSession") UserSessionBean userSession,
        @Valid Task task,
        BindingResult result, Locale locale, Model model
    ) {
        log.info("/project/root/add/task (POST)");
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
            return "project/root/add/task";
        } else {
            task.setProject(null);
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
            return "redirect:/project/root/tasks";
        }
    }
}
