package org.woehlke.simpleworklist.control;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.CategoryService;
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
    protected CategoryService categoryService;

    @Inject
    protected UserService userService;

    @Inject
    protected UserMessageService userMessageService;

    @ModelAttribute("allCategories")
    public final List<Category> getAllCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return categoryService.findAllByUserAccount(user);
    }

    @ModelAttribute("rootCategories")
    public final List<Category> getRootCategories() {
        UserAccount user = userService.retrieveCurrentUser();
        return categoryService.findRootCategoriesByUserAccount(user);
    }

    @ModelAttribute("numberOfNewIncomingMessages")
    public final int getNumberOfNewIncomingMessages(){
        UserAccount user = userService.retrieveCurrentUser();
        return userMessageService.getNumberOfNewIncomingMessagesForUser(user);
    }
}
