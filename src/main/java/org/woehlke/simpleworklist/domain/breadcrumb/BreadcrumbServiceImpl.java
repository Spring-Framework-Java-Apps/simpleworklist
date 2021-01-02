package org.woehlke.simpleworklist.domain.breadcrumb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.domain.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskState;

import java.util.Locale;
import java.util.Stack;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BreadcrumbServiceImpl implements BreadcrumbService {

    private final MessageSource messageSource;

    @Autowired
    public BreadcrumbServiceImpl(MessageSource messageSource) {
        this.messageSource=messageSource;
    }

    @Override
    public Breadcrumb getBreadcrumbForShowRootProject(Locale locale) {
        log.debug("getBreadcrumbForShowRootProject");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProjectRoot();
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, Locale locale) {
        log.debug("getBreadcrumbForShowOneProject");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProjectRoot();
        if(thisProject == null){
            return breadcrumb;
        } else {
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
            return breadcrumb;
        }
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale) {
        log.debug("getBreadcrumbForTaskstate");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code = taskstate.getCode();
        String name = messageSource.getMessage(code,null,locale);
        breadcrumb.addTaskstate(name,taskstate.getUrl());
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task, Locale locale) {
        log.debug("getBreadcrumbForTaskInTaskstate");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addTaskstate(taskstate);
        breadcrumb.addTask(task);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskstateAll(Locale locale) {
        log.debug("getBreadcrumbForTaskstateAll");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="layout.page.all";
        String name= messageSource.getMessage(code,null,locale);
        String url="/taskstate/all";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task, Locale locale) {
        log.debug("getBreadcrumbForTaskInProject");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProject(thisProject);
        breadcrumb.addTask(task);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale) {
        log.debug("getBreadcrumbForUserProfileAndMenu");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangeName(Locale locale) {
        log.debug("getBreadcrumbForUserChangeName");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.name";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/name";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangePassword(Locale locale) {
        log.debug("getBreadcrumbForUserChangePassword");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.password";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/password";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContexts(Locale locale) {
        log.debug("getBreadcrumbForUserContexts");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.contexts";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/contexts";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextAdd(Locale locale) {
        log.debug("getBreadcrumbForUserContextAdd");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.add.context";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/context/add";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextEdit(Locale locale, Context context) {
        log.debug("getBreadcrumbForUserContextEdit");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.edit.context";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/context/edit/"+context.getId();
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextDelete(Locale locale, Context context) {
        log.debug("getBreadcrumbForUserContextDelete");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.delete.context";
        name= messageSource.getMessage(code,null,locale);
        url="/context/delete/"+context.getId();
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale) {
        log.debug("getBreadcrumbForUserChangeLanguage");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.language";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/language";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale) {
        log.debug("getBreadcrumbForMessagesBetweenCurrentAndOtherUser");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.user2user.messages";
        name= messageSource.getMessage(code,null,locale);
        url="/search";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForSearchResults(Locale locale) {
        log.debug("getBreadcrumbForSearchResults");
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.search";
        name= messageSource.getMessage(code,null,locale);
        url="/search";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

}