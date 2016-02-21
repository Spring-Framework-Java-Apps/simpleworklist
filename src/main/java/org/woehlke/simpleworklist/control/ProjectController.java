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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
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

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public final String showCategory(@PathVariable long categoryId) {
        return "redirect:/project/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/page/{pageNumber}", method = RequestMethod.GET)
    public final ModelAndView showCategory(
            @PathVariable long categoryId,
            @PathVariable int pageNumber,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted) {
        ModelAndView mav = new ModelAndView("category/show");
        Project thisProject = null;
        Page<Task> dataLeafPage = null;
        Pageable request =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        if (categoryId != 0) {
            thisProject = projectService.findByCategoryId(categoryId);
            dataLeafPage = taskService.findByCategory(thisProject, request);
        } else {
            thisProject = new Project();
            thisProject.setId(0L);
            dataLeafPage = taskService.findByRootCategory(request);
        }
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        int current = dataLeafPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
        mav.addObject("beginIndex", begin);
        mav.addObject("endIndex", end);
        mav.addObject("currentIndex", current);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisProject);
        mav.addObject("dataList", dataLeafPage.getContent());
        mav.addObject("totalPages", dataLeafPage.getTotalPages());
        if(message != null){
            mav.addObject("message",message);
            mav.addObject("isDeleted",isDeleted);
        }
        return mav;
    }

    @RequestMapping(value = "/category/addchild/{categoryId}", method = RequestMethod.GET)
    public final ModelAndView addNewCategoryForm(@PathVariable long categoryId, Model model) {
        Project thisProject = null;
        Project project = null;
        if (categoryId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            UserAccount userAccount = userService.retrieveCurrentUser();
            thisProject.setUserAccount(userAccount);
            project = Project.newRootCategoryNodeFactory(userAccount);
        } else {
            thisProject = projectService.findByCategoryId(categoryId);
            project = Project.newCategoryNodeFactory(thisProject);
        }
        ModelAndView mav = new ModelAndView("category/add");
        mav.addAllObjects(model.asMap());
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisProject);
        mav.addObject("category", project);
        return mav;
    }

    @RequestMapping(value = "/category/addchild/{categoryId}", method = RequestMethod.POST)
    public final String addNewCategoryStore(@Valid Project project,
                                            @PathVariable long categoryId,
                                            BindingResult result,
                                            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        project.setUserAccount(userAccount);
        if (categoryId == 0) {
            Project thisProject = new Project();
            thisProject.setId(0L);
            model.addAttribute("thisCategory", thisProject);
            project = projectService.saveAndFlush(project);
        } else {
            Project thisProject = projectService.findByCategoryId(categoryId);
            List<Project> children = thisProject.getChildren();
            children.add(project);
            thisProject.setChildren(children);
            project.setParent(thisProject);
            project = projectService.saveAndFlush(project);
            categoryId = project.getId();
            LOGGER.info("project:     "+ project.toString());
            LOGGER.info("thisProject: "+ thisProject.toString());
        }
        return "redirect:/project/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/moveto/{targetCategoryId}", method = RequestMethod.GET)
    public final String moveCategory(
            @PathVariable long categoryId,
            @PathVariable long targetCategoryId) {
        Project thisProject = null;
        if (categoryId != 0) {
            thisProject = projectService.findByCategoryId(categoryId);
            Project targetProject = projectService.findByCategoryId(targetCategoryId);
            projectService.moveCategoryToAnotherCategory(thisProject, targetProject);
        }
        return "redirect:/project/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.GET)
    public final String editCategoryForm(
            @PathVariable long categoryId, Model model) {
        if (categoryId > 0) {
            Project thisProject = projectService.findByCategoryId(categoryId);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisCategory", thisProject);
            model.addAttribute("category", thisProject);
            return "category/edit";
        } else {
            return "redirect:/category/0/page/1";
        }
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.POST)
    public final String editCategoryStore(
            @Valid Project project,
            @PathVariable long categoryId,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            Project thisProject = projectService.findByCategoryId(categoryId);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            return "category/edit";
        } else {
            Project thisProject = projectService.findByCategoryId(project.getId());
            thisProject.setName(project.getName());
            thisProject.setDescription(project.getDescription());
            projectService.saveAndFlush(thisProject);
            return "redirect:/project/" + categoryId + "/page/1";
        }
    }

    @RequestMapping(value = "/category/{nodeId}/delete", method = RequestMethod.GET)
    public final String deleteCategory(
            @PathVariable long nodeId, Model model) {
        long newCategoryId = nodeId;
        if (nodeId > 0) {
            Project project = projectService.findByCategoryId(nodeId);
            boolean hasNoData = taskService.categoryHasNoData(project);
            boolean hasNoChildren = project.hasNoChildren();
            if (hasNoData && hasNoChildren) {
                if (!project.isRootCategory()) {
                    newCategoryId = project.getParent().getId();
                } else {
                    newCategoryId = 0;
                }
                projectService.delete(project);
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
                List<Project> breadcrumb = projectService.getBreadcrumb(project);
                int pageNumber = 1;
                Pageable request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
                Page<Task> dataLeafPage = taskService.findByCategory(project, request);
                int current = dataLeafPage.getNumber() + 1;
                int begin = Math.max(1, current - 5);
                int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
                model.addAttribute("beginIndex", begin);
                model.addAttribute("endIndex", end);
                model.addAttribute("currentIndex", current);
                model.addAttribute("breadcrumb", breadcrumb);
                model.addAttribute("thisCategory", project);
                model.addAttribute("dataList", dataLeafPage.getContent());
                model.addAttribute("totalPages", dataLeafPage.getTotalPages());
                return "category/show";
            }
        }
        return "redirect:/project/" + newCategoryId + "/page/1";
    }
}
