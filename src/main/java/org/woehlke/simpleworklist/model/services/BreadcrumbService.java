package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;
import org.woehlke.simpleworklist.model.beans.Breadcrumb;

import java.util.Locale;

public interface BreadcrumbService {

    Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, UserAccount userAccount);

    Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale);

    Breadcrumb getBreadcrumbForTaskstateAll(Locale locale);

    Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task);

    Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task);

    Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale);

    Breadcrumb getBreadcrumbForUserChangeName(Locale locale);

    Breadcrumb getBreadcrumbForUserChangePassword(Locale locale);

    Breadcrumb getBreadcrumbForUserContexts(Locale locale);

    Breadcrumb getBreadcrumbForUserContextAdd(Locale locale);

    Breadcrumb getBreadcrumbForUserContextEdit(Locale locale);

    Breadcrumb getBreadcrumbForUserContextDelete(Locale locale);

    Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale);

    Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale);

    Breadcrumb getBreadcrumbForSearchResults(Locale locale);

}
