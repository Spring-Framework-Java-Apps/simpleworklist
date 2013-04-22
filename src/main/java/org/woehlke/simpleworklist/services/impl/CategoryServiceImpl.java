package org.woehlke.simpleworklist.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.CategoryRepository;
import org.woehlke.simpleworklist.repository.DataRepository;
import org.woehlke.simpleworklist.services.CategoryService;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Inject
	private CategoryRepository categoryNodeRepository;
	
	@Inject
	private DataRepository dataLeafRepository;
	
	public List<Category> getBreadcrumb(Category thisCategory){
		List<Category> breadcrumb = new ArrayList<Category>();
		Stack<Category> stack = new Stack<Category>();
		Category breadcrumbCategory = thisCategory;
		while(breadcrumbCategory != null){
			stack.push(breadcrumbCategory);
			breadcrumbCategory=breadcrumbCategory.getParent();
		}
		while (!stack.empty()){
			breadcrumb.add(stack.pop());
		}
		return breadcrumb;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void createTestCategoryTree(UserAccount userAccount) {
		Assert.notNull(userAccount);
		logger.info("----------------------------------------------");
		logger.info("createTestCategoryTree");
		logger.info("----------------------------------------------");
		Date nowDate = new Date();
		long now = nowDate.getTime();
		String name01 = "test01_"+now;
		String name02 = "test02_"+now;
		String name0101 = "test0101_"+now;
		String name0102 = "test0102_"+now;
		String name0201 = "test0201_"+now;
		String name0202 = "test0202_"+now;
		String name020201 = "test020201_"+now;
		String name020202 = "test020202_"+now;
		String name020203 = "test020203_"+now;
		String name02020301 = "test02020301_"+now;
		String name02020302 = "test02020302_"+now;
		String name02020303 = "test02020303_"+now;
		Category c01 = Category.newRootCategoryNodeFactory(userAccount);
		Category c02 = Category.newRootCategoryNodeFactory(userAccount);
		c01.setName(name01);
		c02.setName(name02);
		c01.setDescription("description01 for "+name01);	
		c02.setDescription("description02 for "+name02);
		c01=categoryNodeRepository.saveAndFlush(c01);
		c02=categoryNodeRepository.saveAndFlush(c02);
		Category c0101 = Category.newCategoryNodeFactory(c01);
		Category c0102 = Category.newCategoryNodeFactory(c01);
		Category c0201 = Category.newCategoryNodeFactory(c02);
		Category c0202 = Category.newCategoryNodeFactory(c02);
		c0101.setName(name0101);
		c0102.setName(name0102);
		c0201.setName(name0201);
		c0202.setName(name0202);
		c01.getChildren().add(c0101);
		c01.getChildren().add(c0102);
		c02.getChildren().add(c0201);
		c02.getChildren().add(c0202);
		c0101.setDescription("description0101 for "+name0101);
		c0102.setDescription("description0102 for "+name0102);
		c0201.setDescription("description0201 for "+name0201);
		c0202.setDescription("description0202 for "+name0202);
		c0101=categoryNodeRepository.saveAndFlush(c0101);
		c0102=categoryNodeRepository.saveAndFlush(c0102);
		c0201=categoryNodeRepository.saveAndFlush(c0201);
		c0202=categoryNodeRepository.saveAndFlush(c0202);
		Category c020201 = Category.newCategoryNodeFactory(c0202);
		Category c020202 = Category.newCategoryNodeFactory(c0202);
		Category c020203 = Category.newCategoryNodeFactory(c0202);
		c020201.setName(name020201);
		c020202.setName(name020202);
		c020203.setName(name020203);
		c0202.getChildren().add(c020201);
		c0202.getChildren().add(c020202);
		c0202.getChildren().add(c020203);
		c020201.setDescription("description for "+name020201);
		c020202.setDescription("description for "+name020202);
		c020203.setDescription("description for "+name020203);
		c020201=categoryNodeRepository.saveAndFlush(c020201);
		c020202=categoryNodeRepository.saveAndFlush(c020202);
		c020203=categoryNodeRepository.saveAndFlush(c020203);
		Category c02020301 = Category.newCategoryNodeFactory(c020203);
		Category c02020302 = Category.newCategoryNodeFactory(c020203);
		Category c02020303 = Category.newCategoryNodeFactory(c020203);
		c02020301.setName(name02020301);
		c02020302.setName(name02020302);
		c02020303.setName(name02020303);
		c020203.getChildren().add(c02020301);
		c020203.getChildren().add(c02020302);
		c020203.getChildren().add(c02020303);
		c02020301.setDescription("description for "+name02020301);
		c02020302.setDescription("description for "+name02020302);
		c02020303.setDescription("description for "+name02020303);
		c02020301=categoryNodeRepository.saveAndFlush(c02020301);
		c02020302=categoryNodeRepository.saveAndFlush(c02020302);
		c02020303=categoryNodeRepository.saveAndFlush(c02020303);
		for(int i=10;i<111;i++){
			String title = "t_"+i;
			String text = "d_"+i;
			Data d = new Data();
			d.setText(text);
			d.setTitle(title);
			d.setCategory(c02020303);
			dataLeafRepository.saveAndFlush(d);
		}
	}

	@Override
	public List<Category> findByParentIsNull(UserAccount userAccount) {
		return categoryNodeRepository.findByParentIsNullAndUserAccount(userAccount);
	}

	@Override
	public Category findOne(long nodeId) {
		return categoryNodeRepository.findOne(nodeId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public Category saveAndFlush(Category entity) {
		return categoryNodeRepository.saveAndFlush(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void deleteAll() {
		List<Category> root = categoryNodeRepository.findByParentIsNull();
		for(Category rootCategory :root){
			categoryNodeRepository.delete(rootCategory.getId());
		}
	}

	@Override
	public List<Category> findAll(UserAccount userAccount) {
		return categoryNodeRepository.findByUserAccount(userAccount);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void delete(Category thisCategory) {
		Category oldParent = thisCategory.getParent();
		if(oldParent!=null){
			oldParent.getChildren().remove(thisCategory);
			categoryNodeRepository.saveAndFlush(oldParent);
		}
		categoryNodeRepository.delete(thisCategory);
	}

	@Override
	public boolean hasNoData(Category category) {
		return dataLeafRepository.findByCategory(category).isEmpty();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly=false)
	public void moveCategoryToAnotherCategory(Category thisCategory,
			Category targetCategory) {
		Category oldParent = thisCategory.getParent();
		if(oldParent!=null){
			oldParent.getChildren().remove(thisCategory);
			categoryNodeRepository.saveAndFlush(oldParent);
		}
		thisCategory.setParent(targetCategory);
		categoryNodeRepository.saveAndFlush(thisCategory);
	}
}
