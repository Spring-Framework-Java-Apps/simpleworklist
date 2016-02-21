package org.woehlke.simpleworklist.control;

import static org.hamcrest.Matchers.*;
import static org.woehlke.simpleworklist.entities.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.ProjectService;
import org.woehlke.simpleworklist.services.TaskService;
import org.woehlke.simpleworklist.services.UserService;


public class DataAndProjectControllerTest extends AbstractTest {

    private static final Logger logger = LoggerFactory.getLogger(DataAndProjectControllerTest.class);

    @Inject
    private UserService userService;

    @Inject
    private TaskService taskService;

    @Inject
    private ProjectService projectService;

    @Test
    public void testHome() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/")).andDo(print())
                .andExpect(view().name(containsString("redirect:/project/0/page/1")));
    }

    @Test
    public void testCategoryZeroRedirect() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/project/0/")).andDo(print())
                .andExpect(view().name(containsString("redirect:/project/0/page/1")));
    }

    @Test
    public void testCategoryZero() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/project/0/page/1")).andDo(print())
                .andExpect(view().name(containsString("project/show")))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attributeExists("dataList"))
                .andExpect(model().attributeExists("beginIndex"))
                .andExpect(model().attributeExists("endIndex"))
                .andExpect(model().attributeExists("currentIndex"));
    }

    @Test
    public void testFormNewCategoryNode() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/project/addchild/0")).andDo(print())
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attribute("thisCategory", notNullValue()))
                .andExpect(model().attribute("thisCategory", instanceOf(Project.class)))
                .andExpect(model().attribute("thisCategory", hasProperty("id")))
                .andExpect(model().attribute("thisCategory", is(categoryNullObject())));
    }

    @Test
    public void testHelperCategoryCreateTree() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/test/helper/project/createTree")).andDo(print())
                .andExpect(view().name(containsString("redirect:/")));
        UserAccount user = userService.retrieveCurrentUser();
        List<Project> rootCategories = projectService.findRootCategoriesByUserAccount(user);
        Assert.assertTrue(rootCategories.size() > 0);
        for(Project rootProject :rootCategories){
            Assert.assertTrue(rootProject.isRootCategory());
            for(Project child: rootProject.getChildren()){
                Assert.assertFalse(child.isRootCategory());
                Assert.assertEquals(child.getParent().getId().longValue(), rootProject.getId().longValue());
            }
        }
    }

    @Test
    public void testShowChildNodePage() throws Exception {
        makeActiveUser(emails[0]);
        logger.info("----------------------------------------------");
        logger.info("testShowChildNodePage");
        logger.info("----------------------------------------------");
        UserAccount user = userService.retrieveCurrentUser();
        logger.info(user.toString());
        logger.info("----------------------------------------------");
        Date nowDate = new Date();
        long now = nowDate.getTime();
        String name01 = "test01" + now;
        String name02 = "test02" + now;
        String name0101 = "test0101" + now;
        String name0102 = "test0102" + now;
        String name0201 = "test0201" + now;
        String name0202 = "test0202" + now;
        String name020201 = "test020201" + now;
        String name020202 = "test020202" + now;
        String name020203 = "test020203" + now;
        Project c01 = Project.newRootCategoryNodeFactory(user);
        c01.setName(name01);
        c01.setDescription("description01 for " + name01);
        c01 = projectService.saveAndFlush(c01);
        Project c02 = Project.newRootCategoryNodeFactory(user);
        c02.setName(name02);
        c02.setDescription("description02 for " + name02);
        c02 = projectService.saveAndFlush(c02);
        Project c0101 = Project.newCategoryNodeFactory(c01);
        c0101.setName(name0101);
        c0101.setDescription("description0101 for " + name0101);
        c0101 = projectService.saveAndFlush(c0101);
        Project c0102 = Project.newCategoryNodeFactory(c01);
        c0102.setName(name0102);
        c0102.setDescription("description0102 for " + name0102);
        c0102 = projectService.saveAndFlush(c0102);
        Project c0201 = Project.newCategoryNodeFactory(c02);
        c0201.setName(name0201);
        c0201.setDescription("description0201 for " + name0201);
        c0201 = projectService.saveAndFlush(c0201);
        Project c0202 = Project.newCategoryNodeFactory(c02);
        c0202.setName(name0202);
        c0202.setDescription("description0202 for " + name0202);
        c0202 = projectService.saveAndFlush(c0202);
        Project c020201 = Project.newCategoryNodeFactory(c0202);
        Project c020202 = Project.newCategoryNodeFactory(c0202);
        Project c020203 = Project.newCategoryNodeFactory(c0202);
        c020201.setName(name020201);
        c020202.setName(name020202);
        c020203.setName(name020203);
        c020201.setDescription("description for " + name020201);
        c020202.setDescription("description for " + name020202);
        c020203.setDescription("description for " + name020203);
        c020201 = projectService.saveAndFlush(c020201);
        c020202 = projectService.saveAndFlush(c020202);
        c020203 = projectService.saveAndFlush(c020203);
        logger.info(c01.toString());
        logger.info(c02.toString());
        logger.info(c0101.toString());
        logger.info(c0102.toString());
        logger.info(c0201.toString());
        logger.info(c0202.toString());
        logger.info(c020201.toString());
        logger.info(c020202.toString());
        logger.info(c020203.toString());
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/project/" + c01.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("project/show")))
                .andExpect(model().attribute("thisCategory", c01))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/project/" + c0202.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("project/show")))
                .andExpect(model().attribute("thisCategory", c0202))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
        this.mockMvc.perform(
                get("/project/" + c020202.getId() + "/page/1")).andDo(print())
                .andExpect(view().name(containsString("project/show")))
                .andExpect(model().attribute("thisCategory", c020202))
                .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        logger.info("----------------------------------------------");
    }

    @Test
    public void rootCategoriesNonNullPrecondition() throws Exception {
        makeActiveUser(emails[0]);
        UserAccount user = userService.retrieveCurrentUser();
        List<Project> rootCategories = projectService.findRootCategoriesByUserAccount(user);
        for (Project project : rootCategories) {
            this.mockMvc.perform(
                    get("/project/" + project.getId() + "/page/1")).andDo(print())
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("dataList"))
                    .andExpect(view().name(containsString("project/show")))
                    .andExpect(model().attribute("thisCategory", project))
                    .andExpect(model().attribute("thisCategory", is(categoryNotNullObject())));
        }
    }

    @Test
    public void testNewDataLeafForRootCategoryNode() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(
                get("/task/addtocategory/0")).andDo(print())
                .andExpect(model().attributeExists("thisCategory"))
                .andExpect(model().attributeExists("breadcrumb"))
                .andExpect(model().attribute("thisCategory", notNullValue()))
                .andExpect(model().attribute("thisCategory", instanceOf(Project.class)))
                .andExpect(model().attribute("thisCategory", hasProperty("id")))
                .andExpect(model().attribute("thisCategory", is(categoryNullObject())))
                .andExpect(view().name(containsString("task/add")));
    }

    @Test
    public void testUserList() throws Exception {
        makeActiveUser(emails[0]);
        this.mockMvc.perform(get("/users")).andDo(print())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name(containsString("user/users")));
    }

    @Test
    public void testEditDataFormCategory() throws Exception {
        makeActiveUser(emails[0]);
        UserAccount user = userService.retrieveCurrentUser();
        List<Project> rootCategories = projectService.findRootCategoriesByUserAccount(user);
        int i = 0;
        for (Project cat:rootCategories){
            i++;
            Task ai = new Task();
            ai.setProject(cat);
            ai.setTitle("Title_"+i);
            ai.setText("Text_"+i);
            taskService.saveAndFlush(ai);
        }
        for (Project cat:rootCategories){
            int pageNr = 0;
            int pageSize = 10;
            Pageable request = new PageRequest(pageNr, pageSize);
            Page<Task> all = taskService.findByCategory(cat,request);
            for (Task task : all.getContent()) {
                this.mockMvc.perform(
                    get("/task/detail/" + task.getId())).andDo(print())
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("task"))
                    .andExpect(model().attribute("task", notNullValue()))
                    .andExpect(model().attribute("task", instanceOf(Task.class)))
                    .andExpect(model().attribute("task", hasProperty("id")))
                    .andExpect(model().attribute("task", is(actionItemNotNullObject())));
            }
        }
    }


    @Test
    public void testEditDataFormCategory0() throws Exception {
        makeActiveUser(emails[0]);
        for(int i = 100; i<110; i++){
            Task ai = new Task();
            ai.setProject(null);
            ai.setTitle("Title_"+i);
            ai.setText("Text_"+i);
            taskService.saveAndFlush(ai);
        }
        int pageNr = 0;
        int pageSize = 10;
        Pageable request = new PageRequest(pageNr, pageSize);
        Page<Task> all = taskService.findByRootCategory(request);
        for (Task task : all.getContent()) {
            this.mockMvc.perform(
                    get("/task/detail/" + task.getId())).andDo(print())
                    .andExpect(model().attributeExists("thisCategory"))
                    .andExpect(model().attributeExists("breadcrumb"))
                    .andExpect(model().attributeExists("task"))
                    .andExpect(model().attribute("task", notNullValue()))
                    .andExpect(model().attribute("task", instanceOf(Task.class)))
                    .andExpect(model().attribute("task", hasProperty("id")))
                    .andExpect(model().attribute("task", is(actionItemNotNullObject())));
        }
        deleteAll();
    }

}
