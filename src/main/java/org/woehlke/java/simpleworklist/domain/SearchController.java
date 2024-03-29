package org.woehlke.java.simpleworklist.domain;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.search.service.SearchService;
import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.Breadcrumb;
import org.woehlke.java.simpleworklist.domain.meso.breadcrumb.BreadcrumbService;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

/**
 * Created by tw on 14.02.16.
 */
@Log
@Controller
@RequestMapping(path = "/search")
public class SearchController extends AbstractController {

    private final SearchService searchService;

    private final BreadcrumbService breadcrumbService;

    @Autowired
    public SearchController(SearchService searchService, BreadcrumbService breadcrumbService) {
       this.searchService = searchService;
       this.breadcrumbService = breadcrumbService;
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
