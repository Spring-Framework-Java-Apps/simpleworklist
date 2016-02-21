package org.woehlke.simpleworklist.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.ProjectService;
import org.woehlke.simpleworklist.services.UserMessageService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
public abstract class AbstractController {

    @Value("${mvc.controller.pageSize}")
    protected int pageSize;

    @Inject
    protected ProjectService projectService;

    @Inject
    protected UserService userService;

    @Inject
    protected UserMessageService userMessageService;

    @ModelAttribute("allCategories")
    public final List<Project> getAllCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return projectService.findAllByUserAccount(user);
    }

    @ModelAttribute("rootCategories")
    public final List<Project> getRootCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return projectService.findRootCategoriesByUserAccount(user);
    }

    @ModelAttribute("numberOfNewIncomingMessages")
    public final int getNumberOfNewIncomingMessages(){
        UserAccount user = userService.retrieveCurrentUser();
        return userMessageService.getNumberOfNewIncomingMessagesForUser(user);
    }
}
