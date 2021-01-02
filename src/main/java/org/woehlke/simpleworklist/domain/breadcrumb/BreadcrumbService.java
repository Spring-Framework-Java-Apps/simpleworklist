package org.woehlke.simpleworklist.domain.breadcrumb;

import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskState;

import java.util.Locale;

public interface BreadcrumbService {

    Breadcrumb getBreadcrumbForShowRootProject(Locale locale);

    Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, Locale locale);

    Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale);

    Breadcrumb getBreadcrumbForTaskstateAll(Locale locale);

    Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task, Locale locale);

    Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task, Locale locale);

    Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale);

    Breadcrumb getBreadcrumbForUserChangeName(Locale locale);

    Breadcrumb getBreadcrumbForUserChangePassword(Locale locale);

    Breadcrumb getBreadcrumbForUserContexts(Locale locale);

    Breadcrumb getBreadcrumbForUserContextAdd(Locale locale);

    Breadcrumb getBreadcrumbForUserContextEdit(Locale locale, Context context);

    Breadcrumb getBreadcrumbForUserContextDelete(Locale locale, Context context);

    Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale);

    Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale);

    Breadcrumb getBreadcrumbForSearchResults(Locale locale);

}