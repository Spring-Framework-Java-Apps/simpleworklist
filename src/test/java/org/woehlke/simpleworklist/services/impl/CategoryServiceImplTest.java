package org.woehlke.simpleworklist.services.impl;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class CategoryServiceImplTest {

    @Inject
    private CategoryService categoryService;

    @Inject
    private TestService testService;

    @Inject
    private TestHelperService testHelperService;

    @Inject
    private UserService userService;

    private static String emails[] = {"test01@test.de", "test02@test.de", "test03@test.de"};
    private static String passwords[] = {"test01pwd", "test02pwd", "test03pwd"};
    private static String fullnames[] = {"test01 Name", "test02 Name", "test03 Name"};

    private static String username = "undefined@test.de";
    private static String password = "ASDFG";
    private static String name     = "UNDEFINED_NAME";

    private static UserAccount testUser[] = new UserAccount[emails.length];

    static {
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
        }
    }

    private void makeActiveUser(String username) {
        UserDetails ud = userService.loadUserByUsername(username);
        Authentication authRequest = new UsernamePasswordAuthenticationToken(ud.getUsername(), ud.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authRequest);
    }

    private void deleteAll(){
        testHelperService.deleteAllRegistrationProcess();
        testHelperService.deleteAllActionItem();
        testHelperService.deleteAllCategory();
        testHelperService.deleteUserAccount();
        testHelperService.deleteTimelineDay();
        testHelperService.deleteTimelineMonth();
        testHelperService.deleteTimelineYear();
    }

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
