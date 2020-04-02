package org.woehlke.simpleworklist.breadcrumb;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;

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
