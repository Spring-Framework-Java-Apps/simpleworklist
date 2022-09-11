package org.woehlke.java.simpleworklist.domain.meso.breadcrumb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.context.ContextService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.Locale;
import java.util.Optional;
import java.util.Stack;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BreadcrumbServiceImpl implements BreadcrumbService {

    private final MessageSource messageSource;
    private final ContextService contextService;

    @Autowired
    public BreadcrumbServiceImpl(MessageSource messageSource, ContextService contextService) {
        this.messageSource=messageSource;
        this.contextService = contextService;
    }

    @Override
    public Breadcrumb getBreadcrumbForShowProjectRoot(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForShowRootProject");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
        breadcrumb.addProjectRoot();
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForShoProjectId(Project thisProject, Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForShowOneProject");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
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
    public Breadcrumb getBreadcrumbForTaskstate(
        TaskState taskstate,
        Locale locale,
        UserSessionBean userSession
    ) {
        log.info("getBreadcrumbForTaskstate");
        Optional<Context> contextResult = contextService.getContextFor(userSession);
        Context context;
        if(contextResult.isEmpty()){
            context = null;
        } else {
            context = contextResult.get();
        }
        Breadcrumb breadcrumb = new Breadcrumb(locale, context);
        String code = taskstate.getMsgCode();
        String name = messageSource.getMessage(code,null,locale);
        breadcrumb.addTaskstate(name, taskstate.getUrlPath());
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForUserProfileAndMenu");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangeName(Locale locale,UserSessionBean userSession) {
        log.info("getBreadcrumbForUserChangeName");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
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
    public Breadcrumb getBreadcrumbForUserChangePassword(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForUserChangePassword");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
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
    public Breadcrumb getBreadcrumbForUserContexts(Locale locale,UserSessionBean userSession) {
        log.info("getBreadcrumbForUserContexts");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
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
    public Breadcrumb getBreadcrumbForUserContextAdd(Locale locale,UserSessionBean userSession) {
        log.info("getBreadcrumbForUserContextAdd");
        Optional<Context> context = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, context.get());
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
    public Breadcrumb getBreadcrumbForUserContextEdit(Locale locale, Context context, UserSessionBean userSession) {
        log.info("getBreadcrumbForUserContextEdit");
        Optional<Context> contextFromSession = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, contextFromSession.get());
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
    public Breadcrumb getBreadcrumbForUserContextDelete(Locale locale, Context context, UserSessionBean userSession) {
        log.info("getBreadcrumbForUserContextDelete");
        Optional<Context> contextFromSession = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, contextFromSession.get());
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
    public Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForUserChangeLanguage");
        Optional<Context> contextFromSession = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, contextFromSession.get());
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
    public Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForMessagesBetweenCurrentAndOtherUser");
        Optional<Context> contextFromSession = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, contextFromSession.get());
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
    public Breadcrumb getBreadcrumbForSearchResults(Locale locale, UserSessionBean userSession) {
        log.info("getBreadcrumbForSearchResults");
        Optional<Context> contextFromSession = contextService.getContextFor(userSession);
        Breadcrumb breadcrumb = new Breadcrumb(locale, contextFromSession.get());
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
