package org.woehlke.simpleworklist.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.Data;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface CategoryService {

	List<Category> getBreadcrumb(Category thisCategory);
	void createTestCategoryTree(UserAccount userAccount);
	List<Category> findByParentIsNull(UserAccount userAccount);
	Category findOne(long nodeId);
	Category saveAndFlush(Category category);
	void deleteAll();
	List<Category> findAll(UserAccount user);
	void delete(Category category);
	boolean hasNoData(Category category);
	void moveCategoryToAnotherCategory(Category thisCategory,
			Category targetCategory);
}
