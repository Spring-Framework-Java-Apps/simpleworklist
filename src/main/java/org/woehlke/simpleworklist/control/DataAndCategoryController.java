package org.woehlke.simpleworklist.control;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.CategoryService;
import org.woehlke.simpleworklist.services.DataService;
import org.woehlke.simpleworklist.services.TestService;
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class DataAndCategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataAndCategoryController.class);

    @Value("${mvc.controller.pageSize}")
    private int pageSize;

    @Inject
    private TestService testService;

    @Inject
    private CategoryService categoryService;

    @Inject
    private DataService dataService;

    @Inject
    private UserService userService;

    @ModelAttribute("allCategories")
    public final List<Category> getAllCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return categoryService.findAllByUserAccount(user);
    }

    @ModelAttribute("rootCategories")
    public final List<Category> getRootCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return categoryService.findRootCategoriesByUserAccount(user);
    }

    @RequestMapping(value = "/data/detail/{dataId}", method = RequestMethod.GET)
    public final String editDataForm(@PathVariable long dataId, Model model) {
        Data data = dataService.findOne(dataId);
        Category thisCategory = null;
        if (data.getCategory() == null) {
            thisCategory = new Category();
            thisCategory.setId(0L);
        } else {
            thisCategory = data.getCategory();
        }
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("data", data);
        return "data/show";
    }

    @RequestMapping(value = "/data/detail/{dataId}", method = RequestMethod.POST)
    public final String editDataStore(
            @Valid Data data,
            @PathVariable long dataId,
            BindingResult result, Model model) {
        Data persistentData = dataService.findOne(dataId);
        long categoryId = 0;
        Category thisCategory = null;
        if (persistentData.getCategory() == null) {
            thisCategory = new Category();
            thisCategory.setId(0L);
        } else {
            thisCategory = persistentData.getCategory();
            categoryId = thisCategory.getId();
        }
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                LOGGER.info(e.toString());
            }
            model.addAttribute("thisCategory", thisCategory);
            List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            return "/data/detail" + dataId;
        } else {
            persistentData.setTitle(data.getTitle());
            persistentData.setText(data.getText());
            dataService.saveAndFlush(persistentData);
            return "redirect:/category/" + categoryId + "/";
        }

    }

    @RequestMapping(value = "/data/addtocategory/{categoryId}", method = RequestMethod.GET)
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
        Data dataLeaf = new Data();
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("data", dataLeaf);
        return "data/add";
    }

    @RequestMapping(value = "/data/addtocategory/{categoryId}", method = RequestMethod.POST)
    public final String addNewDataToCategoryStore(
            @Valid Data data,
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
            data.setCategory(null);
        } else {
            thisCategory = categoryService.findByCategoryId(categoryId);
            data.setCategory(thisCategory);
        }
        data = dataService.saveAndFlush(data);
        LOGGER.info(data.toString());
        model.addAttribute("thisCategory", thisCategory);
        List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
        model.addAttribute("breadcrumb", breadcrumb);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/data/delete/{dataId}", method = RequestMethod.GET)
    public final String deleteData(@PathVariable long dataId) {
        Data data = dataService.findOne(dataId);
        long categoryId = 0;
        if (data.getCategory() != null) {
            categoryId = data.getCategory().getId();
        }
        dataService.delete(data);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/data/move/{dataId}", method = RequestMethod.GET)
    public final String moveData(@PathVariable long dataId) {
        Data data = dataService.findOne(dataId);
        long categoryId = 0;
        if (data.getCategory() != null) {
            categoryId = data.getCategory().getId();
        }
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/data/{dataId}/moveto/{categoryId}", method = RequestMethod.GET)
    public final String moveDataToAnotherCategory(@PathVariable long dataId,
                                            @PathVariable long categoryId) {
        Data data = dataService.findOne(dataId);
        Category category = categoryService.findByCategoryId(categoryId);
        data.setCategory(category);
        dataService.saveAndFlush(data);
        return "redirect:/category/" + categoryId + "/";
    }

    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public final String showCategory(@PathVariable long categoryId) {
        return "redirect:/category/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/page/{pageNumber}", method = RequestMethod.GET)
    public final ModelAndView showCategory(
            @PathVariable long categoryId,
            @PathVariable int pageNumber) {
        ModelAndView mav = new ModelAndView("category/show");
        Category thisCategory = null;
        Page<Data> dataLeafPage = null;
        Pageable request =
                new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        if (categoryId != 0) {
            thisCategory = categoryService.findByCategoryId(categoryId);
            dataLeafPage = dataService.findByCategory(thisCategory, request);
        } else {
            thisCategory = new Category();
            thisCategory.setId(0L);
            dataLeafPage = dataService.findByRootCategory(request);
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
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public final String home() {
        return "redirect:/category/0/page/1";
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
            thisCategory.getChildren().add(category);
            category = categoryService.saveAndFlush(category);
            LOGGER.info(category.toString());
            List<Category> breadcrumb = categoryService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisCategory", thisCategory);
        }
        return "redirect:/category/" + categoryId + "/page/1";
    }

    @RequestMapping(value = "/test/helper/category/createTree", method = RequestMethod.GET)
    public final String createTestCategoryTree() {
        UserAccount user = userService.retrieveCurrentUser();
        Assert.notNull(user);
        testService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
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
            boolean hasNoData = dataService.categoryHasNoData(category);
            boolean hasNoChildren = category.hasNoChildren();
            if (hasNoData && hasNoChildren) {
                if (!category.isRootCategory()) {
                    newCategoryId = category.getParent().getId();
                } else {
                    newCategoryId = 0;
                }
                categoryService.delete(category);
            } else {
                LOGGER.info("Deletion rejected for Category " + category.getId());
                if (!hasNoData) {
                    LOGGER.warn("Category " + category.getId() + " has data");
                }
                if (!hasNoChildren) {
                    LOGGER.info("Category " + category.getId() + " has children");
                }
            }
        }
        return "redirect:/category/" + newCategoryId + "/page/1";
    }
}
