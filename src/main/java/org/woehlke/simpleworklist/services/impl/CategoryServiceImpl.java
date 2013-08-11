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
