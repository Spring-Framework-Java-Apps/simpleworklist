package org.woehlke.simpleworklist.project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.context.ContextService;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskService;
import org.woehlke.simpleworklist.session.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.List;
import java.util.Locale;

import static org.woehlke.simpleworklist.project.Project.rootProjectId;

@Slf4j
@Service
public class ProjectControllerServiceImpl implements ProjectControllerService {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final ContextService contextService;
    private final BreadcrumbService breadcrumbService;

    @Autowired
    public ProjectControllerServiceImpl(ProjectService projectService, TaskService taskService, ContextService contextService, BreadcrumbService breadcrumbService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.contextService = contextService;
        this.breadcrumbService = breadcrumbService;
    }

    public void addNewProjectToProjectIdForm(
        long projectId,
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    ) {
        log.info("addNewProject projectId="+projectId);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = null;
        Project project = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            project = Project.newRootProjectFactory(userAccount);
            if(userSession.getLastContextId() == 0L){
                model.addAttribute("mustChooseArea", true);
                project.setContext(userAccount.getDefaultContext());
            } else {
                project.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId);
            project = Project.newProjectFactory(thisProject);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
    }

    public String addNewProjectToProjectIdPersist(
        long projectId,
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale,
        Model model
    ){
        log.info("private addNewProjectPersist projectId="+projectId+" "+project.toString());
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        if(result.hasErrors()){
            Project thisProject = null;
            if (projectId == 0) {
                thisProject = new Project();
                thisProject.setId(0L);
            } else {
                thisProject = projectService.findByProjectId(projectId);
            }
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return "project/id/show";
        } else {
            if (projectId == 0) {
                if(userSession.getLastContextId()>0) {
                    project.setContext(context);
                }
                project = projectService.add(project);
                projectId = project.getId();
            } else {
                Project thisProject = projectService.findByProjectId(projectId);
                List<Project> children = thisProject.getChildren();
                children.add(project);
                thisProject.setChildren(children);
                project.setParent(thisProject);
                project = projectService.add(project);
                projectId = project.getId();
                log.info("project:     "+ project.toString());
                log.info("thisProject: "+ thisProject.toString());
            }
            return "redirect:/project/" + projectId;
        }
    }

    public Project getProject(long projectId, UserAccount userAccount, UserSessionBean userSession){
        log.info("getProject");
        Project thisProject;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            if(userSession.getLastContextId() == 0L){
                thisProject.setContext(userAccount.getDefaultContext());
            } else {
                Context context = contextService.findByIdAndUserAccount(userSession.getLastContextId(), userAccount);
                thisProject.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId);
        }
        return thisProject;
    }

    @Override
    public void addNewProjectToProjectRootForm(
        UserSessionBean userSession,
        Context context,
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
        UserSessionBean userSession,
        Project project,
        Context context,
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
