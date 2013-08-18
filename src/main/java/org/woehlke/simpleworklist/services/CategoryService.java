package org.woehlke.simpleworklist.services;

import java.util.List;

import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface CategoryService {

    List<Category> getBreadcrumb(Category thisCategory);

    List<Category> findRootCategoriesByUserAccount(UserAccount userAccount);

    List<Category> findAllByUserAccount(UserAccount user);

    void moveCategoryToAnotherCategory(Category thisCategory, Category targetCategory);

    Category findByCategoryId(long categoryId);

    Category saveAndFlush(Category category);

    void delete(Category category);
}
