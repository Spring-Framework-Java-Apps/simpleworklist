package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.user.UserSessionBean;

import javax.validation.Valid;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(path = "/project/root")
public class ProjectRootController extends AbstractController {

    private final ProjectControllerService projectControllerService;

    private static final long rootProjectId = 0L;

    @Autowired
    public ProjectRootController(ProjectControllerService projectControllerService) {
        this.projectControllerService = projectControllerService;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public final String showRootProject(
        @PageableDefault(sort = "orderIdProject") Pageable pageable,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) boolean isDeleted,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model) {
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
        return "project/root";
    }

    @RequestMapping(path = "/add/new/project", method = RequestMethod.GET)
    public final String addNewTopLevelProjectForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale, Model model
    ){
        log.info("/project/add/new/project (GET)");
        Context context = super.getContext(userSession);
        projectControllerService.addNewProject(rootProjectId, userSession, context, locale, model);
        return "project/addToplevel";
    }

    @RequestMapping(path = "/add/new/project", method = {RequestMethod.POST, RequestMethod.PUT})
    public final String addNewTopLevelProjectSave(
        @Valid Project project,
        @ModelAttribute("userSession") UserSessionBean userSession,
        BindingResult result,
        Locale locale, Model model
    ){
        log.info("/project/add/new/project (POST)");
        Context context = super.getContext(userSession);
        return projectControllerService.addNewProjectPersist( rootProjectId, userSession, project, context,
            result, locale, model, "project/addToplevel");
    }
}
