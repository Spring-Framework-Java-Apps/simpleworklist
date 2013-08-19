package org.woehlke.simpleworklist.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.ActionItemService;
import org.woehlke.simpleworklist.services.CategoryService;
import org.woehlke.simpleworklist.services.TestHelperService;
import org.woehlke.simpleworklist.services.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-context.xml")
public class ActionItemServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionItemServiceImplTest.class);

    @Inject
    private ActionItemService actionItemService;

    @Inject
    private TestHelperService testHelperService;

    @Inject
    private CategoryService categoryService;

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
    public void storeRootData() {
        ActionItem rootActionItem01 = new ActionItem();
        rootActionItem01.setCategory(null);
        rootActionItem01.setText("TEXT01");
        rootActionItem01.setTitle("TITLE01");
        ActionItem rootActionItem02 = new ActionItem();
        rootActionItem02.setCategory(null);
        rootActionItem02.setText("TEXT02");
        rootActionItem02.setTitle("TITLE02");
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem01).getId());
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<ActionItem> list = actionItemService.findByRootCategory(pageRequest).getContent();
        boolean rootData01found = false;
        boolean rootData02found = false;
        for (ActionItem leaf : list) {
            if (leaf.getTitle().contentEquals("TITLE01")) {
                rootData01found = true;
            }
            if (leaf.getTitle().contentEquals("TITLE02")) {
                rootData02found = true;
            }
        }
        Assert.assertTrue(rootData01found);
        Assert.assertTrue(rootData02found);
        deleteAll();
    }

    @Test
    public void testFindOne(){
        ActionItem rootActionItem01 = new ActionItem();
        rootActionItem01.setCategory(null);
        rootActionItem01.setText("TEXT01");
        rootActionItem01.setTitle("TITLE01");
        ActionItem rootActionItem02 = new ActionItem();
        rootActionItem02.setCategory(null);
        rootActionItem02.setText("TEXT02");
        rootActionItem02.setTitle("TITLE02");
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem01).getId());
        Assert.assertNotNull(actionItemService.saveAndFlush(rootActionItem02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<ActionItem> list = actionItemService.findByRootCategory(pageRequest).getContent();
        for (ActionItem leaf : list) {
            ActionItem found =  actionItemService.findOne(leaf.getId());
            Assert.assertEquals(found.getId().longValue(),leaf.getId().longValue());
            actionItemService.delete(leaf);
        }
        List<ActionItem> list2 = actionItemService.findByRootCategory(pageRequest).getContent();
        Assert.assertTrue(list2.size()==0);
    }

    @Test
    public void testCategoryHasNoData(){
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
        Assert.assertTrue(actionItemService.categoryHasNoData(parent));
    }

}
