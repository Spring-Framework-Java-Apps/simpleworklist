package org.woehlke.simpleworklist.services.impl;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.woehlke.simpleworklist.AbstractTest;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImplTest extends AbstractTest {

    @Inject
    private ProjectService projectService;

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
        Project project = new Project();
        project.setName("NAME");
        project.setDescription("DESCRIPTION");
        project.setUserAccount(userAccount);
        project = projectService.saveAndFlush(project);
        projectService.delete(project);
        Assert.assertTrue(projectService.findAllByUserAccount(userAccount).size() == 0);
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
        Project parent = new Project();
        parent.setName("NAME1");
        parent.setDescription("DESCRIPTION1");
        parent.setUserAccount(userAccount);
        parent = projectService.saveAndFlush(parent);
        Project child = new Project();
        child.setName("NAME2");
        child.setDescription("DESC3");
        child.setParent(parent);
        child.setUserAccount(userAccount);
        List<Project> children = new ArrayList<Project>();
        children.add(child);
        parent.setChildren(children);
        projectService.saveAndFlush(child);
        projectService.saveAndFlush(parent);
        projectService.delete(child);
        SecurityContextHolder.clearContext();
        Assert.assertTrue(projectService.findAllByUserAccount(userAccount).size() == 1);
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
        Project parent = new Project();
        parent.setName("NAME1");
        parent.setDescription("DESCRIPTION1");
        parent.setUserAccount(userAccount);
        parent = projectService.saveAndFlush(parent);
        Project child = new Project();
        child.setName("NAME2");
        child.setDescription("DESC3");
        child.setParent(parent);
        child.setUserAccount(userAccount);
        List<Project> children = new ArrayList<Project>();
        children.add(child);
        parent.setChildren(children);
        Project grandchild = new Project();
        grandchild.setName("NAME3");
        grandchild.setDescription("DESC3");
        grandchild.setParent(parent);
        grandchild.setUserAccount(userAccount);
        List<Project> grandchildren = new ArrayList<Project>();
        grandchildren.add(grandchild);
        child.setChildren(grandchildren);
        projectService.saveAndFlush(child);
        projectService.saveAndFlush(parent);
        projectService.saveAndFlush(grandchild);
        projectService.moveCategoryToAnotherCategory(grandchild,parent);
        SecurityContextHolder.clearContext();
    }

}
