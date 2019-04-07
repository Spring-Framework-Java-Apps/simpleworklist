package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.model.entities.Project;
import org.woehlke.simpleworklist.model.entities.Task;
import org.woehlke.simpleworklist.model.entities.UserAccount;
import org.woehlke.simpleworklist.model.Breadcrumb;
import org.woehlke.simpleworklist.model.services.BreadcrumbService;

import java.util.Stack;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BreadcrumbServiceImpl implements BreadcrumbService {

    @Override
    public Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, UserAccount userAccount) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.addProjectRoot();
        if(thisProject.getUserAccount().getId().longValue() == userAccount.getId().longValue()) {
            if (thisProject.getId() > 0) {
                Stack<Project> stack = new Stack<>();
                Project breadcrumbProject = thisProject;
                while (breadcrumbProject != null) {
                    stack.push(breadcrumbProject);
                    breadcrumbProject = breadcrumbProject.getParent();
                }
                while (!stack.empty()) {
                    breadcrumb.addProject(stack.pop());
                }
            }
        }
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskstate(String taskstate) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.addTaskstate(taskstate);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.addTaskstate(taskstate);
        breadcrumb.addTask(task);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.addProject(thisProject);
        breadcrumb.addTask(task);
        return breadcrumb;
    }
}
