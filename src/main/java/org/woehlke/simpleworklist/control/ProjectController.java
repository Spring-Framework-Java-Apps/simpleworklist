package org.woehlke.simpleworklist.control;

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
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.UserSessionBean;
import org.woehlke.simpleworklist.services.TaskService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class ProjectController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Inject
    private TaskService taskService;

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public final String showProject(@PathVariable long projectId) {
        return "redirect:/project/" + projectId + "/page/1";
    }

    @RequestMapping(value = "/project/{projectId}/page/{pageNumber}", method = RequestMethod.GET)
    public final String showProject(
            @PathVariable long projectId,
            @PathVariable int pageNumber,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted,
            @ModelAttribute("areaId") UserSessionBean areaId,
            BindingResult result,Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Area area = areaService.findByIdAndUserAccount(areaId.getAreaId(), userAccount);
        Project thisProject = null;
        Page<Task> taskPage = null;
        Pageable pageRequest =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        if (projectId != 0) {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            if(areaId.getAreaId() == 0) {
                taskPage = taskService.findByProject(thisProject, pageRequest, userAccount);
            } else {
                taskPage = taskService.findByProject(thisProject, pageRequest, userAccount, area);
            }
        } else {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            if(areaId.getAreaId() == 0) {
                taskPage = taskService.findByRootProject(pageRequest, userAccount);
            } else {
                taskPage = taskService.findByRootProject(pageRequest, userAccount, area);
            }
        }
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        if(taskPage != null){
            model.addAttribute("dataList", taskPage.getContent());
            model.addAttribute("totalPages", taskPage.getTotalPages());
        }
        if(message != null){
            model.addAttribute("message",message);
            model.addAttribute("isDeleted",isDeleted);
        }
        return "project/show";
    }

    @RequestMapping(value = "/project/addchild", method = RequestMethod.GET)
    public final String addNewProjectForm(Model model){
        return addNewProjectForm(0,model);
    }

    @RequestMapping(value = "/project/addchild/{projectId}", method = RequestMethod.GET)
    public final String addNewProjectForm(@PathVariable long projectId, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Project thisProject = null;
        Project project = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            thisProject.setUserAccount(userAccount);
            project = Project.newRootProjectFactory(userAccount);
        } else {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            project = Project.newProjectFactory(thisProject);
        }
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
        return "project/add";
    }

    @RequestMapping(value = "/project/addchild/{projectId}",
            method = RequestMethod.POST)
    public final String addNewProjectStore(
            @PathVariable long projectId,
            @ModelAttribute("areaId") UserSessionBean areaId,
            @Valid Project project,
            BindingResult result,
            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        if(result.hasErrors()){
            Project thisProject = null;
            if (projectId == 0) {
                thisProject = new Project();
                thisProject.setId(0L);
                thisProject.setUserAccount(userAccount);
            } else {
                thisProject = projectService.findByProjectId(projectId, userAccount);
            }
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject, userAccount);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return "project/add";
        } else {
            project.setUserAccount(userAccount);
            if(areaId.getAreaId()>0) {
                Area area = areaService.findByIdAndUserAccount(areaId.getAreaId(), userAccount);
                project.setArea(area);
            }
            if (projectId == 0) {
                project = projectService.saveAndFlush(project, userAccount);
            } else {
                Project thisProject = projectService.findByProjectId(projectId, userAccount);
                List<Project> children = thisProject.getChildren();
                children.add(project);
                thisProject.setChildren(children);
                project.setParent(thisProject);
                project = projectService.saveAndFlush(project, userAccount);
                projectId = project.getId();
                LOGGER.info("project:     "+ project.toString());
                LOGGER.info("thisProject: "+ thisProject.toString());
            }
            return "redirect:/project/" + projectId + "/page/1";
        }
    }

    @RequestMapping(value = "/project/{projectId}/moveto/{targetProjectId}", method = RequestMethod.GET)
    public final String moveProject(
            @PathVariable long projectId,
            @PathVariable long targetProjectId) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        Project thisProject = null;
        if (projectId != 0) {
            thisProject = projectService.findByProjectId(projectId, userAccount);
            Project targetProject = projectService.findByProjectId(targetProjectId, userAccount);
            projectService.moveProjectToAnotherProject(thisProject, targetProject,userAccount );
        }
        return "redirect:/project/" + projectId + "/page/1";
    }

    @RequestMapping(value = "/project/{projectId}/edit", method = RequestMethod.GET)
    public final String editProjectForm(
            @PathVariable long projectId, Model model) {
        if (projectId > 0) {
            UserAccount userAccount = userService.retrieveCurrentUser();
            List<Area> areas = areaService.getAllForUser(userAccount);
            Project thisProject = projectService.findByProjectId(projectId, userAccount);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject,userAccount );
            model.addAttribute("areas",areas);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", thisProject);
            return "project/edit";
        } else {
            return "redirect:/category/0/page/1";
        }
    }

    @RequestMapping(value = "/project/{projectId}/edit", method = RequestMethod.POST)
    public final String editProjectStore(
            @PathVariable long projectId,
            @Valid Project project,
            BindingResult result, Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            Project thisProject = projectService.findByProjectId(projectId, userAccount);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject,userAccount );
            model.addAttribute("breadcrumb", breadcrumb);
            return "project/edit";
        } else {
            Project thisProject = projectService.findByProjectId(project.getId(), userAccount);
            thisProject.setName(project.getName());
            thisProject.setDescription(project.getDescription());
            Area newArea = project.getArea();
            boolean areaChanged = (newArea.getId().longValue() != thisProject.getArea().getId().longValue());
            if(areaChanged){
                newArea = areaService.findByIdAndUserAccount(newArea.getId().longValue(), userAccount);
                projectService.moveProjectToAnotherArea(thisProject,newArea, userAccount);
                model.addAttribute("areaId", new UserSessionBean(newArea.getId().longValue()));
            } else {
                projectService.saveAndFlush(thisProject, userAccount);
            }
            return "redirect:/project/" + projectId + "/page/1";
        }
    }

    @RequestMapping(value = "/project/{projectId}/delete", method = RequestMethod.GET)
    public final String deleteProject(
            @PathVariable long projectId, Model model) {
        long newProjectId = projectId;
        UserAccount userAccount = userService.retrieveCurrentUser();
        if (projectId > 0) {
            Project project = projectService.findByProjectId(projectId, userAccount);
            if(project != null){
                boolean hasNoData = taskService.projectHasNoTasks(project, userAccount);
                boolean hasNoChildren = project.hasNoChildren();
                if (hasNoData && hasNoChildren) {
                    if (!project.isRootCategory()) {
                        newProjectId = project.getParent().getId();
                    } else {
                        newProjectId = 0;
                    }
                    projectService.delete(project, userAccount);
                    String message = "Project is deleted. You see its parent project now.";
                    model.addAttribute("message",message);
                    model.addAttribute("isDeleted",true);
                } else {
                    StringBuilder s = new StringBuilder("Deletion rejected for this Project, because ");
                    LOGGER.info("Deletion rejected for Project " + project.getId());
                    if (!hasNoData) {
                        LOGGER.warn("Project " + project.getId() + " has actionItem");
                        s.append("Project has actionItems.");
                    }
                    if (!hasNoChildren) {
                        LOGGER.info("Project " + project.getId() + " has children");
                        s.append("Project has child categories.");
                    }
                    model.addAttribute("message",s.toString());
                    model.addAttribute("isDeleted",false);
                    List<Project> breadcrumb = projectService.getBreadcrumb(project, userAccount);
                    int pageNumber = 1;
                    Pageable request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
                    Page<Task> dataLeafPage = taskService.findByProject(project, request, userAccount);
                    int current = dataLeafPage.getNumber() + 1;
                    int begin = Math.max(1, current - 5);
                    int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
                    model.addAttribute("beginIndex", begin);
                    model.addAttribute("endIndex", end);
                    model.addAttribute("currentIndex", current);
                    model.addAttribute("breadcrumb", breadcrumb);
                    model.addAttribute("thisProject", project);
                    model.addAttribute("dataList", dataLeafPage.getContent());
                    model.addAttribute("totalPages", dataLeafPage.getTotalPages());
                    return "project/show";
                }
            }
        }
        return "redirect:/project/" + newProjectId + "/page/1";
    }
}
