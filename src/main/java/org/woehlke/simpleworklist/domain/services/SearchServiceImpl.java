package org.woehlke.simpleworklist.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.search.SearchRequest;
import org.woehlke.simpleworklist.search.SearchResult;
import org.woehlke.simpleworklist.search.services.SearchRequestService;
import org.woehlke.simpleworklist.search.services.SearchResultService;
import org.woehlke.simpleworklist.search.services.SearchService;


/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private final SearchRequestService searchRequestService;
    private final SearchResultService searchResultService;

    public SearchServiceImpl(SearchRequestService searchRequestService, SearchResultService searchResultService) {
        this.searchRequestService = searchRequestService;
        this.searchResultService = searchResultService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SearchResult search(SearchRequest searchRequest) {
        log.info("search");
        searchRequest = searchRequestService.update(searchRequest);
        SearchResult result = new SearchResult();
        result.setSearchRequest(searchRequest);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SearchResult search(String searchterm, Context context) {
        log.info("search");
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSearchterm(searchterm);
        searchRequest.setContext(context);
        searchRequest = searchRequestService.add(searchRequest);
        SearchResult result = new SearchResult();
        result.setSearchRequest(searchRequest);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetSearchIndex() {
        log.info("resetSearchIndex");
        searchResultService.resetSearchIndex();
    }
}
