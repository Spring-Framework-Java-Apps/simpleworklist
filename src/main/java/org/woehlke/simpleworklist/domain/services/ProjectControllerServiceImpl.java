package org.woehlke.simpleworklist.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.application.breadcrumb.services.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.project.services.ProjectControllerService;
import org.woehlke.simpleworklist.project.services.ProjectService;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.services.TaskService;
import org.woehlke.simpleworklist.user.session.UserSessionBean;
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
    private final TaskService taskService;

    @Autowired
    public ProjectControllerServiceImpl(
        ProjectService projectService,
        BreadcrumbService breadcrumbService,
        TaskService taskService) {
        this.projectService = projectService;
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
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
        Project thisProject = projectService.findByProjectId(projectId);
        Project project = Project.newProjectFactoryForParentProject(thisProject);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
        model.addAttribute("userSession", userSession);
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
            model.addAttribute("userSession", userSession);
            return "project/id/show";
        } else {
            Project thisProject = projectService.findByProjectId(projectId);
            project = thisProject.addOtherProjectToChildren(project);
            project.setContext(context);
            project = projectService.add(project);
            thisProject = projectService.update(thisProject);
            log.info("project:     "+ project.toString());
            log.info("thisProject: "+ thisProject.toString());
            model.addAttribute("userSession", userSession);
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
        model.addAttribute("userSession", userSession);
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
        project.setContext(context);
        project = projectService.add(project);
        userSession.setLastProjectId(project.getId());
        model.addAttribute("userSession", userSession);
        return project.getUrl();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTaskToTaskAndChangeTaskOrderInProject(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        Project project = sourceTask.getProject();
        log.info("-------------------------------------------------------------------------------");
        log.info(" START: moveTaskToTaskAndChangeTaskOrderInProject ");
        log.info("        "+project.out()+":");
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        boolean okProject = destinationTask.hasProject(project);
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameProject = sourceTask.hasSameProjectAs(destinationTask);
        boolean go = sameContext && sameProject && okProject;
        if (go) {
            boolean srcIsBelowDestinationTask  = sourceTask.isBelowByProject(destinationTask);
            log.info(" srcIsBelowDestinationTask: "+srcIsBelowDestinationTask);
            log.info("-------------------------------------------------------------------------------");
            if (srcIsBelowDestinationTask) {
                this.taskService.moveTasksDownByProject(sourceTask, destinationTask);
            } else {
                this.taskService.moveTasksUpByProject(sourceTask, destinationTask);
            }
        }
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTaskToTaskAndChangeTaskOrderInProject ");
        log.info("        "+project.out()+":");
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveTaskToTaskAndChangeTaskOrderInProjectRoot(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        log.info("-------------------------------------------------------------------------------");
        log.info(" START: moveTaskToTaskAndChangeTaskOrderIn Project Root");
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
        boolean sourceTaskRoot = destinationTask.isInRootProject();
        boolean destinationTaskRoot = destinationTask.isInRootProject();
        boolean sameContext = sourceTask.hasSameContextAs(destinationTask);
        boolean sameProject = sourceTask.hasSameProjectAs(destinationTask);
        boolean go = sameContext && sameProject && sourceTaskRoot && destinationTaskRoot;
        if ( go ) {
            boolean srcIsBelowDestinationTask  = sourceTask.isBelowByProject(destinationTask);
            log.info(" srcIsBelowDestinationTask: "+srcIsBelowDestinationTask);
            log.info("-------------------------------------------------------------------------------");
            if (srcIsBelowDestinationTask) {
                this.taskService.moveTasksDownByProjectRoot(sourceTask, destinationTask);
            } else {
                this.taskService.moveTasksUpByProjectRoot(sourceTask, destinationTask);
            }
        }
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTaskToTaskAndChangeTaskOrderIn Project Root");
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }
}
