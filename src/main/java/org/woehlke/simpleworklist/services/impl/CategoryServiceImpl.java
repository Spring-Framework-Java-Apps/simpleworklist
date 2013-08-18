package org.woehlke.simpleworklist.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.repository.CategoryRepository;
import org.woehlke.simpleworklist.services.CategoryService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    @Inject
    private CategoryRepository categoryRepository;

    public List<Category> getBreadcrumb(Category thisCategory) {
        List<Category> breadcrumb = new ArrayList<Category>();
        Stack<Category> stack = new Stack<Category>();
        Category breadcrumbCategory = thisCategory;
        while (breadcrumbCategory != null) {
            stack.push(breadcrumbCategory);
            breadcrumbCategory = breadcrumbCategory.getParent();
        }
        while (!stack.empty()) {
            breadcrumb.add(stack.pop());
        }
        return breadcrumb;
    }

    @Override
    public List<Category> findRootCategoriesByUserAccount(UserAccount userAccount) {
        return categoryRepository.findByParentIsNullAndUserAccount(userAccount);
    }

    @Override
    public Category findByCategoryId(long categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Category saveAndFlush(Category entity) {
        return categoryRepository.saveAndFlush(entity);
    }

    @Override
    public List<Category> findAllByUserAccount(UserAccount userAccount) {
        return categoryRepository.findByUserAccount(userAccount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Category thisCategory) {
        Category oldParent = thisCategory.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisCategory);
            categoryRepository.saveAndFlush(oldParent);
        }
        categoryRepository.delete(thisCategory);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveCategoryToAnotherCategory(Category thisCategory,
                                              Category targetCategory) {
        Category oldParent = thisCategory.getParent();
        if (oldParent != null) {
            oldParent.getChildren().remove(thisCategory);
            categoryRepository.saveAndFlush(oldParent);
        }
        thisCategory.setParent(targetCategory);
        categoryRepository.saveAndFlush(thisCategory);
    }
}
