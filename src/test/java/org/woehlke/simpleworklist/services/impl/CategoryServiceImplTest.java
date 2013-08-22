package org.woehlke.simpleworklist.services.impl;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImplTest extends AbstractTest {

    @Inject
    private CategoryService categoryService;

    @Inject
    private UserService userService;

    @Test
    public void testDeleteWithoutParent(){
        deleteAll();
        for(UserAccount user:testUser){
            userService.saveAndFlush(user);
        }
        makeActiveUser(emails[0]);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Category category = new Category();
        category.setName("NAME");
        category.setDescription("DESCRIPTION");
        category.setUserAccount(userAccount);
        category = categoryService.saveAndFlush(category);
        categoryService.delete(category);
        Assert.assertTrue(categoryService.findAllByUserAccount(userAccount).size() == 0);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDeleteWithParent(){
        deleteAll();
        for(UserAccount user:testUser){
            userService.saveAndFlush(user);
        }
        makeActiveUser(emails[0]);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Category parent = new Category();
        parent.setName("NAME1");
        parent.setDescription("DESCRIPTION1");
        parent.setUserAccount(userAccount);
        parent = categoryService.saveAndFlush(parent);
        Category child = new Category();
        child.setName("NAME2");
        child.setDescription("DESC3");
        child.setParent(parent);
        child.setUserAccount(userAccount);
        List<Category> children = new ArrayList<Category>();
        children.add(child);
        parent.setChildren(children);
        categoryService.saveAndFlush(child);
        categoryService.saveAndFlush(parent);
        categoryService.delete(child);
        SecurityContextHolder.clearContext();
        Assert.assertTrue(categoryService.findAllByUserAccount(userAccount).size() == 1);
        deleteAll();
    }

    @Test
    public void testMoveCategoryToAnotherCategory(){
        deleteAll();
        for(UserAccount user:testUser){
            userService.saveAndFlush(user);
        }
        makeActiveUser(emails[0]);
        UserAccount userAccount = userService.retrieveCurrentUser();
        Category parent = new Category();
        parent.setName("NAME1");
        parent.setDescription("DESCRIPTION1");
        parent.setUserAccount(userAccount);
        parent = categoryService.saveAndFlush(parent);
        Category child = new Category();
        child.setName("NAME2");
        child.setDescription("DESC3");
        child.setParent(parent);
        child.setUserAccount(userAccount);
        List<Category> children = new ArrayList<Category>();
        children.add(child);
        parent.setChildren(children);
        Category grandchild = new Category();
        grandchild.setName("NAME3");
        grandchild.setDescription("DESC3");
        grandchild.setParent(parent);
        grandchild.setUserAccount(userAccount);
        List<Category> grandchildren = new ArrayList<Category>();
        grandchildren.add(grandchild);
        child.setChildren(grandchildren);
        categoryService.saveAndFlush(child);
        categoryService.saveAndFlush(parent);
        categoryService.saveAndFlush(grandchild);
        categoryService.moveCategoryToAnotherCategory(grandchild,parent);
        SecurityContextHolder.clearContext();
    }

}
