package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.SearchResult;
import org.woehlke.simpleworklist.services.CategoryService;
import org.woehlke.simpleworklist.services.SearchService;
import org.woehlke.simpleworklist.services.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @Inject
    private SearchService searchService;

    @Inject
    private CategoryService categoryService;

    @Inject
    private UserService userService;

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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public final String loginForm(@RequestParam String searchterm, Model model) {
        LOGGER.info("Search: "+searchterm);
        SearchResult searchResult = searchService.search(searchterm);
        LOGGER.info("found: "+searchResult.toString());
        model.addAttribute("searchResult",searchResult);
        return "search/resultlist";
    }
}
