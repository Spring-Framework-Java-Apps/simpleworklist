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
import org.woehlke.simpleworklist.entities.ActionState;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.ActionItemService;

@Controller
public class ActionItemAndCategoryController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionItemAndCategoryController.class);

    @Inject
    private ActionItemService actionItemService;

    @RequestMapping(value = "/actionItem/detail/{dataId}", method = RequestMethod.GET)
    public final String editDataForm(@PathVariable long dataId, Model model) {
        ActionItem actionItem = actionItemService.findOne(dataId);
        Category thisCategory = null;
        if (actionItem.getCategory() == null) {
            thisCategory = new Category();
            thisCategory.setId(0L);
        } else {
            thisCategory = actionItem.getCategory();
        }
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("actionItem", actionItem);
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
            @Valid ActionItem actionItem,
            BindingResult result, Model model) {
        ActionItem persistentActionItem = actionItemService.findOne(dataId);
        long categoryId = 0;
        Category thisCategory = null;
        if (persistentActionItem.getCategory() == null) {
            thisCategory = new Category();
            thisCategory.setId(0L);
        } else {
            thisCategory = persistentActionItem.getCategory();
            categoryId = thisCategory.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            model.addAttribute("thisCategory", thisCategory);
            List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            return "/actionItem/detail" + dataId;
        } else {
            persistentActionItem.setTitle(actionItem.getTitle());
            persistentActionItem.setText(actionItem.getText());
            persistentActionItem.setStatus(actionItem.getStatus());
            actionItemService.saveAndFlush(persistentActionItem);
            return "redirect:/category/" + categoryId + "/";
        }

    }

    @RequestMapping(value = "/actionItem/addtocategory/{categoryId}", method = RequestMethod.GET)
    public final String addNewDataToCategoryForm(
            @PathVariable long categoryId,
            Model model) {
        Category thisCategory = null;
        if (categoryId == 0) {
            thisCategory = new Category();
            thisCategory.setId(0L);
        } else {
            thisCategory = categoryService.findByCategoryId(categoryId);
        }
        ActionItem actionItemLeaf = new ActionItem();
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("data", actionItemLeaf);
        List<ActionState> stateValues = new ArrayList<>();
        for(ActionState state:ActionState.values()){
            stateValues.add(state);
        }
        model.addAttribute("stateValues", ActionState.values());
        return "actionItem/add";
    }

    @RequestMapping(value = "/actionItem/addtocategory/{categoryId}", method = RequestMethod.POST)
    public final String addNewDataToCategoryStore(
            @Valid ActionItem actionItem,
            @PathVariable long categoryId,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
        }
        Category thisCategory = null;
        if (categoryId == 0) {
            thisCategory = new Category();
            thisCategory.setId(0L);
            actionItem.setCategory(null);
        } else {
            thisCategory = categoryService.findByCategoryId(categoryId);
            actionItem.setCategory(thisCategory);
        }
        actionItem = actionItemService.saveAndFlush(actionItem);
        LOGGER.info(actionItem.toString());
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/delete/{dataId}", method = RequestMethod.GET)
    public final String deleteData(@PathVariable long dataId) {
        ActionItem actionItem = actionItemService.findOne(dataId);
        long categoryId = 0;
        if (actionItem.getCategory() != null) {
            categoryId = actionItem.getCategory().getId();
        }
        actionItemService.delete(actionItem);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/move/{dataId}", method = RequestMethod.GET)
    public final String moveData(@PathVariable long dataId) {
        ActionItem actionItem = actionItemService.findOne(dataId);
        long categoryId = 0;
        if (actionItem.getCategory() != null) {
            categoryId = actionItem.getCategory().getId();
        }
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/{dataId}/moveto/{categoryId}", method = RequestMethod.GET)
    public final String moveDataToAnotherCategory(@PathVariable long dataId,
                                            @PathVariable long categoryId) {
        ActionItem actionItem = actionItemService.findOne(dataId);
        Category category = categoryService.findByCategoryId(categoryId);
        actionItem.setCategory(category);
        actionItemService.saveAndFlush(actionItem);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/actionItem/transform/{dataId}", method = RequestMethod.GET)
    public final String transformActionItemIntoCategory(@PathVariable long dataId) {
        ActionItem actionItem = actionItemService.findOne(dataId);
        long categoryId = 0;
        if (actionItem.getCategory() != null) {
            categoryId = actionItem.getCategory().getId();
        }
        Category parentCategory = categoryService.findByCategoryId(categoryId);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Category thisCategory = new Category();
        thisCategory.setParent(parentCategory);
        thisCategory.setUserAccount(userAccount);
        thisCategory.setName(actionItem.getTitle());
        thisCategory.setDescription(actionItem.getText());
        thisCategory.setUuid(actionItem.getUuid());
        thisCategory = categoryService.saveAndFlush(thisCategory);
        actionItemService.delete(actionItem);
        categoryId = thisCategory.getId();
        LOGGER.info("tried to transform ActionItem "+dataId+" to new Category "+categoryId);
        return "redirect:/category/" + categoryId + "/";
    }
}
