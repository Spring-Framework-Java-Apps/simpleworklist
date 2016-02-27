package org.woehlke.simpleworklist.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.TaskService;
import org.woehlke.simpleworklist.services.ProjectService;
import org.woehlke.simpleworklist.services.UserService;

public class TaskServiceImplTest extends AbstractTest {

    @Inject
    private TaskService taskService;

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Test
    public void storeRootData() {
        Task rootTask01 = new Task();
        rootTask01.setProject(null);
        rootTask01.setText("TEXT01");
        rootTask01.setTitle("TITLE01");
        Task rootTask02 = new Task();
        rootTask02.setProject(null);
        rootTask02.setText("TEXT02");
        rootTask02.setTitle("TITLE02");
        Assert.assertNotNull(taskService.saveAndFlush(rootTask01).getId());
        Assert.assertNotNull(taskService.saveAndFlush(rootTask02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<Task> list = taskService.findByRootProject(pageRequest).getContent();
        boolean rootData01found = false;
        boolean rootData02found = false;
        for (Task leaf : list) {
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
        Task rootTask01 = new Task();
        rootTask01.setProject(null);
        rootTask01.setText("TEXT01");
        rootTask01.setTitle("TITLE01");
        Task rootTask02 = new Task();
        rootTask02.setProject(null);
        rootTask02.setText("TEXT02");
        rootTask02.setTitle("TITLE02");
        Assert.assertNotNull(taskService.saveAndFlush(rootTask01).getId());
        Assert.assertNotNull(taskService.saveAndFlush(rootTask02).getId());
        Pageable pageRequest = new PageRequest(0, 10);
        List<Task> list = taskService.findByRootProject(pageRequest).getContent();
        for (Task leaf : list) {
            Task found =  taskService.findOne(leaf.getId());
            Assert.assertEquals(found.getId().longValue(),leaf.getId().longValue());
            taskService.delete(leaf);
        }
        List<Task> list2 = taskService.findByRootProject(pageRequest).getContent();
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
        Project parent = new Project();
        parent.setName("NAME1");
        parent.setDescription("DESCRIPTION1");
        parent.setUserAccount(userAccount);
        parent = projectService.saveAndFlush(parent);
        Assert.assertTrue(taskService.projectHasNoTasks(parent));
    }

}
