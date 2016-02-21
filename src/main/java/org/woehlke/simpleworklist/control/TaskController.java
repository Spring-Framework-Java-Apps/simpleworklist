package org.woehlke.simpleworklist.control;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.enumerations.ActionState;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TaskService;

@Controller
public class TaskController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Inject
    private TaskService taskService;

    @RequestMapping(value = "/actionItem/detail/{dataId}", method = RequestMethod.GET)
    public final String editDataForm(@PathVariable long dataId, Model model) {
        Task task = taskService.findOne(dataId);
        Project thisProject = null;
        if (task.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = task.getProject();
        }
        model.addAttribute("thisCategory", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("actionItem", task);
        List<ActionState> stateValues = new ArrayList<>();
        for(ActionState state:ActionState.values()){
            stateValues.add(state);
        }
        model.addAttribute("stateValues", ActionState.values());
        return "actionItem/show";
    }

    @RequestMapping(value = "/actionItem/detail/{dataId}", method = RequestMethod.POST)
    public final String editDataStore(
            @PathVariable long dataId,
            @Valid Task task,
            BindingResult result, Model model) {
        Task persistentTask = taskService.findOne(dataId);
        long categoryId = 0;
        Project thisProject = null;
        if (persistentTask.getProject() == null) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = persistentTask.getProject();
            categoryId = thisProject.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            model.addAttribute("thisCategory", thisProject);
            List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
            model.addAttribute("breadcrumb", breadcrumb);
            return "/task/detail" + dataId;
        } else {
            persistentTask.setTitle(task.getTitle());
            persistentTask.setText(task.getText());
            persistentTask.setStatus(task.getStatus());
            taskService.saveAndFlush(persistentTask);
            return "redirect:/category/" + categoryId + "/";
        }

    }

    @RequestMapping(value = "/actionItem/addtocategory/{categoryId}", method = RequestMethod.GET)
    public final String addNewDataToCategoryForm(
            @PathVariable long categoryId,
            Model model) {
        Project thisProject = null;
        if (categoryId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
        } else {
            thisProject = projectService.findByCategoryId(categoryId);
        }
        Task taskLeaf = new Task();
        model.addAttribute("thisCategory", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("data", taskLeaf);
        List<ActionState> stateValues = new ArrayList<>();
        for(ActionState state:ActionState.values()){
            stateValues.add(state);
        }
        model.addAttribute("stateValues", ActionState.values());
        return "actionItem/add";
    }

    @RequestMapping(value = "/actionItem/addtocategory/{categoryId}", method = RequestMethod.POST)
    public final String addNewDataToCategoryStore(
            @Valid Task task,
            @PathVariable long categoryId,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
        }
        Project thisProject = null;
        if (categoryId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            task.setProject(null);
        } else {
            thisProject = projectService.findByCategoryId(categoryId);
            task.setProject(thisProject);
        }
        task = taskService.saveAndFlush(task);
        LOGGER.info(task.toString());
        model.addAttribute("thisCategory", thisProject);
        List<Project> breadcrumb = projectService.getBreadcrumb(thisProject);
        model.addAttribute("breadcrumb", breadcrumb);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/delete/{dataId}", method = RequestMethod.GET)
    public final String deleteData(@PathVariable long dataId) {
        Task task = taskService.findOne(dataId);
        long categoryId = 0;
        if (task.getProject() != null) {
            categoryId = task.getProject().getId();
        }
        taskService.delete(task);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/move/{dataId}", method = RequestMethod.GET)
    public final String moveData(@PathVariable long dataId) {
        Task task = taskService.findOne(dataId);
        long categoryId = 0;
        if (task.getProject() != null) {
            categoryId = task.getProject().getId();
        }
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/{dataId}/moveto/{categoryId}", method = RequestMethod.GET)
    public final String moveDataToAnotherCategory(@PathVariable long dataId,
                                            @PathVariable long categoryId) {
        Task task = taskService.findOne(dataId);
        Project project = projectService.findByCategoryId(categoryId);
        task.setProject(project);
        taskService.saveAndFlush(task);
        return "redirect:/project/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/transform/{dataId}", method = RequestMethod.GET)
    public final String transformActionItemIntoCategory(@PathVariable long dataId) {
        Task task = taskService.findOne(dataId);
        long categoryId = 0;
        if (task.getProject() != null) {
            categoryId = task.getProject().getId();
        }
        Project parentProject = projectService.findByCategoryId(categoryId);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Project thisProject = new Project();
        thisProject.setParent(parentProject);
        thisProject.setUserAccount(userAccount);
        thisProject.setName(task.getTitle());
        thisProject.setDescription(task.getText());
        thisProject.setUuid(task.getUuid());
        thisProject = projectService.saveAndFlush(thisProject);
        taskService.delete(task);
        categoryId = thisProject.getId();
        LOGGER.info("tried to transform Task "+dataId+" to new Project "+categoryId);
        return "redirect:/category/" + categoryId + "/";
    }
}
