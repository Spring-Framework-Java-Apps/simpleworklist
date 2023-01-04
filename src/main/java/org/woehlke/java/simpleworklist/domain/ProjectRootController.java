package org.woehlke.java.simpleworklist.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.meso.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskEnergy;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskTime;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskLifecycleService;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMoveService;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Slf4j
@Controller
@RequestMapping(path = "/project/root")
public class ProjectRootController extends AbstractController {

    private final ProjectControllerService projectControllerService;
    private final TaskService taskService;
    private final BreadcrumbService breadcrumbService;

    @Autowired
    public ProjectRootController(
      ProjectControllerService projectControllerService,
      TaskService taskService, BreadcrumbService breadcrumbService) {
      this.projectControllerService = projectControllerService;
      this.taskService = taskService;
      this.breadcrumbService = breadcrumbService;
    }

    @RequestMapping(path="", method = RequestMethod.GET)
    public final String projectRoot(
        @PageableDefault(sort = "orderIdProject", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) boolean isDeleted,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ) {
        log.info("/project/root");
        Context context = super.getContext(userSession);
        userSession.setLastProjectId(0L);
        model.addAttribute("userSession",userSession);
        Page<Task> taskPage = taskService.findByProjectRoot(context,pageable);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowProjectRoot(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("taskPage", taskPage);
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
        }
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getSlug());
        model.addAttribute("dataPage", true);
        return "project/root/show";
    }

    @RequestMapping(path = "/project/add", method = RequestMethod.GET)
    public final String projectRootAddProjectGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ){
        log.info("/project/root/project/add (GET)");
        Context context = super.getContext(userSession);
        projectControllerService.addNewProjectToProjectRootForm(userSession, context, locale, model);
        model.addAttribute("userSession", userSession);
        model.addAttribute("taskstateType",PROJECTS.getSlug());
        model.addAttribute("dataPage", true);
        return "project/root/project/add";
    }

    @RequestMapping(path = "/project/add", method = RequestMethod.POST)
    public final String projectRootAddProjectPost(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ) {
        log.info("/project/root/add/project (POST)");
        Context context = super.getContext(userSession);
        if (result.hasErrors()) {
            model.addAttribute("userSession", userSession);
            return "project/root/project/add";
        } else {
            project.setUuid(UUID.randomUUID());
            return projectControllerService.addNewProjectToProjectRootPersist(
                userSession,
                project,
                context,
                result,
                locale,
                model
            );
        }
    }
}
