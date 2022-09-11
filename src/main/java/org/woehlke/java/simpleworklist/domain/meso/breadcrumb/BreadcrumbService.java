package org.woehlke.java.simpleworklist.domain.meso.breadcrumb;

import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.TaskState;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.Locale;

public interface BreadcrumbService {

    Breadcrumb getBreadcrumbForShowRootProject(Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, Locale locale, UserSessionBean userSession);

    Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale, UserSessionBean userSession);

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
