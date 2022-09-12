package org.woehlke.java.simpleworklist.domain.db.data.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.project.ProjectControllerService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskService;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.task.TaskMove2TaskService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;

import static org.woehlke.java.simpleworklist.domain.db.data.task.TaskState.PROJECTS;

@Slf4j
@Service
public class ProjectControllerServiceImpl implements ProjectControllerService {

    private final ProjectService projectService;
    private final BreadcrumbService breadcrumbService;
    private final TaskService taskService;
    private final TaskMove2TaskService taskMove2TaskService;

    @Autowired
    public ProjectControllerServiceImpl(
      ProjectService projectService,
      BreadcrumbService breadcrumbService,
      TaskService taskService, TaskMove2TaskService taskMove2TaskService) {
        this.projectService = projectService;
        this.breadcrumbService = breadcrumbService;
        this.taskService = taskService;
      this.taskMove2TaskService = taskMove2TaskService;
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
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject,locale,userSession);
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
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShoProjectId(thisProject,locale,userSession);
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

    @Override
    public Project findByProjectId(@Min(1L) long projectId) {
      log.info("findByProjectId");
      return projectService.findByProjectId(projectId);
    }

  @Override
  public Page<Task> findByProject(Project thisProject, Pageable pageable) {
    return taskService.findByProject(thisProject,pageable
    );
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
        project.setId(Project.rootProjectId);
        project.setContext(context);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowProjectRoot(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("project", project);
        model.addAttribute("thisProjectId", project.getId());
        model.addAttribute("breadcrumb", breadcrumb);
        userSession.setLastProjectId(Project.rootProjectId);
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
        model.addAttribute("myTaskState",PROJECTS.getSlug());
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
                this.taskMove2TaskService.moveTasksDownByProject(sourceTask, destinationTask);
            } else {
                this.taskMove2TaskService.moveTasksUpByProject(sourceTask, destinationTask);
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
                this.taskMove2TaskService.moveTasksDownByProjectRoot(sourceTask, destinationTask);
            } else {
                this.taskMove2TaskService.moveTasksUpByProjectRoot(sourceTask, destinationTask);
            }
        }
        log.info("-------------------------------------------------------------------------------");
        log.info(" DONE: moveTaskToTaskAndChangeTaskOrderIn Project Root");
        log.info("        "+sourceTask.outProject()+" -> "+destinationTask.outProject());
        log.info("-------------------------------------------------------------------------------");
    }
}
