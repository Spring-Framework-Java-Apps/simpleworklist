package org.woehlke.simpleworklist.control;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(DataAndCategoryController.class);

    private static final int PAGE_SIZE = 10;

    @Inject
    private TestService testService;

	@Inject
	private CategoryService categoryNodeService;
	
	@Inject
	private DataService dataLeafService;
	
	@Inject
	private UserService userService;

    @ModelAttribute("allCategories")
    public List<Category> allCategories(){
        UserAccount user=userService.retrieveCurrentUser();
        return categoryNodeService.findAll(user);
    }

	@ModelAttribute("rootCategories")
	public List<Category> rootCategories(){
		UserAccount user=userService.retrieveCurrentUser();
		return categoryNodeService.findByParentIsNull(user);
	}

	@RequestMapping(value = "/data/detail/{dataId}", method = RequestMethod.GET)
	public String editDataLeafForm(@PathVariable long dataId, Model model){
		Data data = dataLeafService.findOne(dataId);
		Category thisCategory = null;
		if(data.getCategory()==null){
			thisCategory = new Category();
			thisCategory.setId(0L);
		} else {
			thisCategory = data.getCategory();
		}
		model.addAttribute("thisCategory", thisCategory);
		List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
		model.addAttribute("breadcrumb", breadcrumb);
		model.addAttribute("data", data);
		return "data/show";
	}
	
	@RequestMapping(value = "/data/detail/{dataId}", method = RequestMethod.POST)
	public String editDataLeafStore(
			@Valid Data data,
			@PathVariable long dataId,
			BindingResult result, Model model){
		Data persistentData = dataLeafService.findOne(dataId);
		long categoryId = 0;
		Category thisCategory = null;
		if(persistentData.getCategory()==null){
			thisCategory = new Category();
			thisCategory.setId(0L);
		} else {
			thisCategory = persistentData.getCategory();
			categoryId = thisCategory.getId();
		}
		if(result.hasErrors()){
			for(ObjectError e :result.getAllErrors()){
				logger.info(e.toString());
			}
			model.addAttribute("thisCategory", thisCategory);
			List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
			model.addAttribute("breadcrumb", breadcrumb);
			return "/data/detail"+dataId;
		} else {
			persistentData.setTitle(data.getTitle());
			persistentData.setText(data.getText());
			dataLeafService.saveAndFlush(persistentData);
			return "redirect:/category/"+categoryId+"/";
		}
		
	}

	@RequestMapping(value = "/data/addtocategory/{categoryId}", method = RequestMethod.GET)
	public String addNewCategoryNodeForm(
			@PathVariable long categoryId, 
			Model model){
		Category thisCategory = null;
		if(categoryId==0){
			thisCategory = new Category();
			thisCategory.setId(0L);
		} else {
			thisCategory = categoryNodeService.findOne(categoryId);
		} 
		Data dataLeaf = new Data();
		model.addAttribute("thisCategory", thisCategory);
		List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
		model.addAttribute("breadcrumb", breadcrumb);
		model.addAttribute("data", dataLeaf);
		return "data/add";
	}
	
	@RequestMapping(value = "/data/addtocategory/{categoryId}", method = RequestMethod.POST)
    public String addNewCategoryNodeStore( 
    		@Valid Data data, 
    		@PathVariable long categoryId, 
    		BindingResult result, Model model) {
		if(result.hasErrors()){
			for(ObjectError e :result.getAllErrors()){
				logger.info(e.toString());
			}
		}
		Category thisCategory = null;
		if(categoryId==0){
			thisCategory = new Category();
			thisCategory.setId(0L);
			data.setCategory(null);
		} else {
			thisCategory = categoryNodeService.findOne(categoryId);
			data.setCategory(thisCategory);
		}
		data = dataLeafService.saveAndFlush(data);
		logger.info(data.toString());
		model.addAttribute("thisCategory", thisCategory);
		List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
		model.addAttribute("breadcrumb", breadcrumb);
		return "redirect:/category/"+categoryId+"/";
	}
	
	@RequestMapping(value = "/data/delete/{dataId}", method = RequestMethod.GET)
	public String deleteDataLeaf(@PathVariable long dataId){
		Data data = dataLeafService.findOne(dataId);
		long categoryId = 0;
		if(data.getCategory()!=null){
			categoryId = data.getCategory().getId();
		}
		dataLeafService.delete(data);
		return "redirect:/category/"+categoryId+"/";
	}
	
	@RequestMapping(value = "/data/move/{dataId}", method = RequestMethod.GET)	
	public String moveDataLeaf(@PathVariable long dataId){
		Data data = dataLeafService.findOne(dataId);
		long categoryId = 0;
		if(data.getCategory()!=null){
			categoryId = data.getCategory().getId();
		}
		return "redirect:/category/"+categoryId+"/";
	}
	
	@RequestMapping(value = "/data/{dataId}/moveto/{categoryId}", method = RequestMethod.GET)
	public String moveDataToCategory(@PathVariable long dataId,
			@PathVariable long categoryId){
		Data data = dataLeafService.findOne(dataId);
		Category category = categoryNodeService.findOne(categoryId);
		data.setCategory(category);
		dataLeafService.saveAndFlush(data);
		return "redirect:/category/"+categoryId+"/";
	}



    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public String showCategoryNode(@PathVariable long categoryId){
        return "redirect:/category/"+categoryId+"/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/page/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView showCategoryNode(
            @PathVariable long categoryId,
            @PathVariable int pageNumber){
        ModelAndView mav = new ModelAndView("category/show");
        Category thisCategory = null;
        Page<Data> dataLeafPage = null;
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "title");
        if(categoryId != 0){
            thisCategory = categoryNodeService.findOne(categoryId);
            dataLeafPage = dataLeafService.findByCategory(thisCategory,request);
        } else {
            thisCategory = new Category();
            thisCategory.setId(0L);
            dataLeafPage = dataLeafService.findByCategoryIsNull(request);
        }
        List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
        int current = dataLeafPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, dataLeafPage.getTotalPages());
        mav.addObject("beginIndex", begin);
        mav.addObject("endIndex", end);
        mav.addObject("currentIndex", current);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisCategory);
        mav.addObject("dataList", dataLeafPage.getContent());
        mav.addObject("totalPages",dataLeafPage.getTotalPages());
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        return "redirect:/category/0/page/1";
    }

    @RequestMapping(value = "/category/addchild/{categoryId}", method = RequestMethod.GET)
    public ModelAndView formNewCategoryNode(@PathVariable long categoryId, Model model){
        Category thisCategory = null;
        Category category = null;
        if(categoryId==0){
            thisCategory = new Category();
            thisCategory.setId(0L);
            UserAccount userAccount=userService.retrieveCurrentUser();
            thisCategory.setUserAccount(userAccount);
            category = Category.newRootCategoryNodeFactory(userAccount);
        } else {
            thisCategory = categoryNodeService.findOne(categoryId);
            category = Category.newCategoryNodeFactory(thisCategory);
        }
        ModelAndView mav = new ModelAndView("category/add");
        mav.addAllObjects(model.asMap());
        List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
        mav.addObject("breadcrumb", breadcrumb);
        mav.addObject("thisCategory", thisCategory);
        mav.addObject("category", category);
        return mav;
    }

    @RequestMapping(value = "/category/addchild/{categoryId}", method = RequestMethod.POST)
    public String addNewCategoryNode(@Valid Category category, @PathVariable long categoryId, BindingResult result, Model model) {
        UserAccount userAccount=userService.retrieveCurrentUser();
        category.setUserAccount(userAccount);
        if(categoryId==0){
            Category thisCategory = new Category();
            thisCategory.setId(0L);
            model.addAttribute("thisCategory", thisCategory);
            category = categoryNodeService.saveAndFlush(category);
        } else {
            Category thisCategory = categoryNodeService.findOne(categoryId);
            thisCategory.getChildren().add(category);
            category = categoryNodeService.saveAndFlush(category);
            logger.info(category.toString());
            List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisCategory", thisCategory);
        }
        return "redirect:/category/"+categoryId+"/page/1";
    }

    @RequestMapping(value = "/test/helper/category/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user=userService.retrieveCurrentUser();
        Assert.notNull(user);
        testService.createTestCategoryTree(user);
        return "redirect:/";
    }

    @RequestMapping(value = "/category/{categoryId}/moveto/{targetCategoryId}", method = RequestMethod.GET)
    public String moveCategoryNode(
            @PathVariable long categoryId,
            @PathVariable long targetCategoryId){
        Category thisCategory = null;
        if(categoryId != 0){
            thisCategory = categoryNodeService.findOne(categoryId);
            Category targetCategory=categoryNodeService.findOne(targetCategoryId);
            categoryNodeService.moveCategoryToAnotherCategory(thisCategory,targetCategory);
        }
        return "redirect:/category/"+categoryId+"/page/1";
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.GET)
    public String editCategoryForm(
            @PathVariable long categoryId, Model model){
        if(categoryId > 0){
            Category thisCategory = categoryNodeService.findOne(categoryId);
            List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisCategory", thisCategory);
            model.addAttribute("category", thisCategory);
            return "category/edit";
        } else {
            return "redirect:/category/0/page/1";
        }
    }

    @RequestMapping(value = "/category/{categoryId}/edit", method = RequestMethod.POST)
    public String editCategorySave(
            @Valid Category category,
            @PathVariable long categoryId,
            BindingResult result, Model model) {
        if(result.hasErrors()){
            for(ObjectError e :result.getAllErrors()){
                logger.info(e.toString());
            }
            Category thisCategory = categoryNodeService.findOne(categoryId);
            List<Category> breadcrumb = categoryNodeService.getBreadcrumb(thisCategory);
            model.addAttribute("breadcrumb", breadcrumb);
            return "category/edit";
        } else {
            Category thisCategory = categoryNodeService.findOne(category.getId());
            thisCategory.setName(category.getName());
            thisCategory.setDescription(category.getDescription());
            categoryNodeService.saveAndFlush(thisCategory);
            return "redirect:/category/"+categoryId+"/page/1";
        }
    }

    @RequestMapping(value = "/category/{nodeId}/delete", method = RequestMethod.GET)
    public String deleteCategory(
            @PathVariable long nodeId, Model model){
        long newCategoryId = nodeId;
        if(nodeId>0){
            Category category = categoryNodeService.findOne(nodeId);
            boolean hasNoData = dataLeafService.hasNoData(category);
            boolean hasNoChildren = category.hasNoChildren();
            if(hasNoData && hasNoChildren){
                if(!category.isRootCategory()){
                    newCategoryId = category.getParent().getId();
                } else {
                    newCategoryId = 0;
                }
                categoryNodeService.delete(category);
            } else {
                logger.info("Deletion rejected for Category "+category.getId());
                if (!hasNoData){
                    logger.warn("Category "+category.getId()+" has data");
                }
                if(!hasNoChildren){
                    logger.info("Category "+category.getId()+" has children");
                }
            }
        }
        return "redirect:/category/"+newCategoryId+"/page/1";
    }
}
