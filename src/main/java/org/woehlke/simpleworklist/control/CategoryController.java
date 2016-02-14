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
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.ActionItemService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class CategoryController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Inject
    private ActionItemService actionItemService;

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public final String showCategory(@PathVariable long categoryId) {
        return "redirect:/category/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/page/{pageNumber}", method = RequestMethod.GET)
    public final ModelAndView showCategory(
            @PathVariable long categoryId,
            @PathVariable int pageNumber,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) boolean isDeleted) {
        ModelAndView mav = new ModelAndView("category/show");
        Category thisCategory = null;
        Page<ActionItem> dataLeafPage = null;
        Pageable request =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        if (categoryId != 0) {
            thisCategory = categoryService.findByCategoryId(categoryId);
            dataLeafPage = actionItemService.findByCategory(thisCategory, request);
        } else {
            thisCategory = new Category();
            thisCategory.setId(0L);
            dataLeafPage = actionItemService.findByRootCategory(request);
        }
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        int current = dataLeafPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
        mav.addObject("beginIndex", begin);
        mav.addObject("endIndex", end);
        mav.addObject("currentIndex", current);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisCategory);
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
        Category thisCategory = null;
        Category category = null;
        if (categoryId == 0) {
            thisCategory = new Category();
            thisCategory.setId(0L);
            UserAccount userAccount = userService.retrieveCurrentUser();
            thisCategory.setUserAccount(userAccount);
            category = Category.newRootCategoryNodeFactory(userAccount);
        } else {
            thisCategory = categoryService.findByCategoryId(categoryId);
            category = Category.newCategoryNodeFactory(thisCategory);
        }
        ModelAndView mav = new ModelAndView("category/add");
        mav.addAllObjects(model.asMap());
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisCategory);
        mav.addObject("category", category);
        return mav;
    }

    @RequestMapping(value = "/category/addchild/{categoryId}", method = RequestMethod.POST)
    public final String addNewCategoryStore(@Valid Category category,
                                            @PathVariable long categoryId,
                                            BindingResult result,
                                            Model model) {
        UserAccount userAccount = userService.retrieveCurrentUser();
        category.setUserAccount(userAccount);
        if (categoryId == 0) {
            Category thisCategory = new Category();
            thisCategory.setId(0L);
            model.addAttribute("thisCategory", thisCategory);
            category = categoryService.saveAndFlush(category);
        } else {
            Category thisCategory = categoryService.findByCategoryId(categoryId);
            List<Category> children = thisCategory.getChildren();
            children.add(category);
            thisCategory.setChildren(children);
            category.setParent(thisCategory);
            category = categoryService.saveAndFlush(category);
            categoryId = category.getId();
            LOGGER.info("category:     "+category.toString());
            LOGGER.info("thisCategory: "+thisCategory.toString());
        }
        return "redirect:/category/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/moveto/{targetCategoryId}", method = RequestMethod.GET)
    public final String moveCategory(
            @PathVariable long categoryId,
            @PathVariable long targetCategoryId) {
        Category thisCategory = null;
        if (categoryId != 0) {
            thisCategory = categoryService.findByCategoryId(categoryId);
            Category targetCategory = categoryService.findByCategoryId(targetCategoryId);
            categoryService.moveCategoryToAnotherCategory(thisCategory, targetCategory);
        }
        return "redirect:/category/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.GET)
    public final String editCategoryForm(
            @PathVariable long categoryId, Model model) {
        if (categoryId > 0) {
            Category thisCategory = categoryService.findByCategoryId(categoryId);
            List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisCategory", thisCategory);
            model.addAttribute("category", thisCategory);
            return "category/edit";
        } else {
            return "redirect:/category/0/page/1";
        }
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.POST)
    public final String editCategoryStore(
            @Valid Category category,
            @PathVariable long categoryId,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            Category thisCategory = categoryService.findByCategoryId(categoryId);
            List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            return "category/edit";
        } else {
            Category thisCategory = categoryService.findByCategoryId(category.getId());
            thisCategory.setName(category.getName());
            thisCategory.setDescription(category.getDescription());
            categoryService.saveAndFlush(thisCategory);
            return "redirect:/category/" + categoryId + "/page/1";
        }
    }

    @RequestMapping(value = "/category/{nodeId}/delete", method = RequestMethod.GET)
    public final String deleteCategory(
            @PathVariable long nodeId, Model model) {
        long newCategoryId = nodeId;
        if (nodeId > 0) {
            Category category = categoryService.findByCategoryId(nodeId);
            boolean hasNoData = actionItemService.categoryHasNoData(category);
            boolean hasNoChildren = category.hasNoChildren();
            if (hasNoData && hasNoChildren) {
                if (!category.isRootCategory()) {
                    newCategoryId = category.getParent().getId();
                } else {
                    newCategoryId = 0;
                }
                categoryService.delete(category);
                String message = "Category is deleted. You see its parent category now.";
                model.addAttribute("message",message);
                model.addAttribute("isDeleted",true);
            } else {
                StringBuilder s = new StringBuilder("Deletion rejected for this Category, because ");
                LOGGER.info("Deletion rejected for Category " + category.getId());
                if (!hasNoData) {
                    LOGGER.warn("Category " + category.getId() + " has actionItem");
                    s.append("Category has actionItems.");
                }
                if (!hasNoChildren) {
                    LOGGER.info("Category " + category.getId() + " has children");
                    s.append("Category has child categories.");
                }
                model.addAttribute("message",s.toString());
                model.addAttribute("isDeleted",false);
                List<Category> breadcrumb = categoryService.getBreadcrumb(category);
                int pageNumber = 1;
                Pageable request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
                Page<ActionItem> dataLeafPage = actionItemService.findByCategory(category, request);
                int current = dataLeafPage.getNumber() + 1;
                int begin = Math.max(1, current - 5);
                int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
                model.addAttribute("beginIndex", begin);
                model.addAttribute("endIndex", end);
                model.addAttribute("currentIndex", current);
                model.addAttribute("breadcrumb", breadcrumb);
                model.addAttribute("thisCategory", category);
                model.addAttribute("dataList", dataLeafPage.getContent());
                model.addAttribute("totalPages", dataLeafPage.getTotalPages());
                return "category/show";
            }
        }
        return "redirect:/category/" + newCategoryId + "/page/1";
    }
}
