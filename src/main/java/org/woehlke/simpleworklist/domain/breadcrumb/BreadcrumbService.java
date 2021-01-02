package org.woehlke.simpleworklist.domain.breadcrumb;

import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskState;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import javax.validation.constraints.NotNull;
import java.util.Locale;

public interface BreadcrumbService {

    Breadcrumb getBreadcrumbForShowRootProject(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForTaskstateAll(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task, Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task, Locale local, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale,UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserChangeName(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserChangePassword(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserContexts(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserContextAdd(Locale locale,UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserContextEdit(Locale locale, Context context, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserContextDelete(Locale locale, Context context, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForSearchResults(Locale locale, UserSessionBean userSession);

}
