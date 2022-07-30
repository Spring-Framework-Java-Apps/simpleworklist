package org.woehlke.java.simpleworklist.domain.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.java.simpleworklist.application.framework.AbstractController;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.search.result.SearchResult;
import org.woehlke.java.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.session.UserSessionBean;

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

    @RequestMapping(path = "", method = RequestMethod.GET)
    public final String searchGet(
            @RequestParam String searchterm,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale, Model model
    ) {
        log.info("searchResults");
        Context context = super.getContext(userSession);
        userSession.setLastSearchterm(searchterm);
        model.addAttribute("userSession", userSession);
        log.info("Search: "+ searchterm);
        SearchResult searchResult = searchService.search(searchterm, context);
        log.info("found: "+ searchResult.toString());
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForSearchResults(locale,userSession);
        model.addAttribute("searchResult",searchResult);
        model.addAttribute("breadcrumb",breadcrumb);
        return "search/resultlist";
    }
}
