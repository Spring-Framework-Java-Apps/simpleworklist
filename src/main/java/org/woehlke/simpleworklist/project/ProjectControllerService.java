package org.woehlke.simpleworklist.project;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.session.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.Locale;

public interface ProjectControllerService {

    void addNewProject(
        long projectId,
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    );

    String addNewProjectPersist(
        long projectId,
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale,
        Model model,
        String template
    );

    Project getProject(
        long projectId,
        UserAccount userAccount,
        UserSessionBean userSession
    );

    void addNewProjectToRoot(
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    );

    String addNewProjectToRootPersist(
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale,
        Model model
    );

}
