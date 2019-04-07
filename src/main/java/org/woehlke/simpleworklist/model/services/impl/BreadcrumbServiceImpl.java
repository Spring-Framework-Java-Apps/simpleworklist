package org.woehlke.simpleworklist.model.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.model.Breadcrumb;
import org.woehlke.simpleworklist.model.services.BreadcrumbService;

import java.util.Locale;
import java.util.Stack;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BreadcrumbServiceImpl implements BreadcrumbService {

    private final MessageSource messageSource;

    @Autowired
    public BreadcrumbServiceImpl(MessageSource messageSource) {
        this.messageSource=messageSource;
    }

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
    public Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb();
        String code = taskstate.getCode();
        String name = messageSource.getMessage(code,null,locale);
        breadcrumb.addTaskstate(name,taskstate.getUrl());
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
    public Breadcrumb getBreadcrumbForTaskstateAll(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb();
        String code="layout.page.all";
        String name= messageSource.getMessage(code,null,locale);
        String url="/taskstate/all";
        breadcrumb.addPage(name,url);
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
