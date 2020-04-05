package org.woehlke.simpleworklist.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.common.AbstractController;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Controller
@RequestMapping(path = "/search")
public class SearchController extends AbstractController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
       this.searchService = searchService;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public final String searchResults(
            @RequestParam String searchterm,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        Context context = super.getContext(userSession);
        UserAccount thisUser = context.getUserAccount();
        userSession.setLastSearchterm(searchterm);
        model.addAttribute("userSession",userSession);
        log.info("Search: "+ searchterm);
        SearchResult searchResult = searchService.search(searchterm, thisUser);
        log.info("found: "+ searchResult.toString());
        model.addAttribute("searchResult",searchResult);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForSearchResults(locale);
        model.addAttribute("breadcrumb",breadcrumb);
        return "search/resultlist";
    }
}
