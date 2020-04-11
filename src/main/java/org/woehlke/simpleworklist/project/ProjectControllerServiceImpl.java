package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.session.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;

import static org.woehlke.simpleworklist.project.Project.rootProjectId;

@Slf4j
@Service
public class ProjectControllerServiceImpl implements ProjectControllerService {

    private final ProjectService projectService;
    private final BreadcrumbService breadcrumbService;

    @Autowired
    public ProjectControllerServiceImpl(
        ProjectService projectService,
        BreadcrumbService breadcrumbService
    ) {
        this.projectService = projectService;
        this.breadcrumbService = breadcrumbService;
    }

    public void addNewProjectToProjectIdForm(
        @Min(1L) long projectId,
        @NotNull UserSessionBean userSession,
        @NotNull Context context,
        @NotNull Locale locale,
        @NotNull Model model
    ) {
        log.info("addNewProject projectId="+projectId);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = projectService.findByProjectId(projectId);
        Project project = Project.newProjectFactoryForParentProject(thisProject);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
    }

    public String addNewProjectToProjectIdPersist(
        @Min(1L) long projectId,
        @NotNull UserSessionBean userSession,
        @NotNull Project project,
        @NotNull Context context,
        BindingResult result,
        Locale locale,
        Model model
    ){
        log.info("private addNewProjectPersist projectId="+projectId+" "+project.toString());
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        if(result.hasErrors()){
            Project thisProject = projectService.findByProjectId(projectId);
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return "project/id/show";
        } else {
            Project thisProject = projectService.findByProjectId(projectId);
            project = thisProject.addOtherProjectToChildren(project);
            project = projectService.add(project);
            thisProject = projectService.update(thisProject);
            log.info("project:     "+ project.toString());
            log.info("thisProject: "+ thisProject.toString());
            return thisProject.getUrl();
        }
    }

    public Project getProject(
        @Min(1L) long projectId,
        @NotNull UserAccount userAccount,
        @NotNull UserSessionBean userSession
    ){
        log.info("getProject");
        return projectService.findByProjectId(projectId);
    }

    @Override
    public void addNewProjectToProjectRootForm(
        @NotNull UserSessionBean userSession,
        @NotNull Context context,
        Locale locale,
        Model model
    ) {
        log.info("addNewProjectToRoot");
        Project project;
        project = new Project();
        project.setId(rootProjectId);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowRootProject(locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("project", project);
        model.addAttribute("thisProjectId", project.getId());
        model.addAttribute("breadcrumb", breadcrumb);
        userSession.setLastProjectId(rootProjectId);
    }

    @Override
    public String addNewProjectToProjectRootPersist(
        @NotNull UserSessionBean userSession,
        @NotNull Project project,
        @NotNull Context context,
        BindingResult result,
        Locale locale,
        Model model
    ) {
        log.info("addNewProjectToRootPersist");
        project = projectService.add(project);
        userSession.setLastProjectId(project.getId());
        return project.getUrl();
    }
}
