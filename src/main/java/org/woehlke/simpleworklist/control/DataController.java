package org.woehlke.simpleworklist.control;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class DataController {

	private static final Logger logger = LoggerFactory.getLogger(DataController.class);
	
	@Inject
	private CategoryService categoryNodeService;
	
	@Inject
	private DataService dataLeafService;
	
	@Inject
	private UserService userService;

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
}
