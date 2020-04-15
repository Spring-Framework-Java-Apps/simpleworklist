package org.woehlke.simpleworklist.application.breadcrumb.services;

import org.woehlke.simpleworklist.application.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskState;

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
