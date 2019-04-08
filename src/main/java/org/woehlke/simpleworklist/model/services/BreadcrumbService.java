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

}
