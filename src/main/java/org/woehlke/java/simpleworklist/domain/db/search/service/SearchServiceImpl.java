package org.woehlke.java.simpleworklist.domain.db.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.search.SearchRequest;
import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;
import org.woehlke.java.simpleworklist.domain.db.search.request.SearchRequestService;
import org.woehlke.java.simpleworklist.domain.db.search.result.SearchResultService;


/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private final SearchRequestService searchRequestService;
    private final SearchResultService searchResultService;

    @Autowired
    public SearchServiceImpl(SearchRequestService searchRequestService, SearchResultService searchResultService) {
        this.searchRequestService = searchRequestService;
        this.searchResultService = searchResultService;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SearchResult search(SearchRequest searchRequest) {
        log.info("search");
        searchRequest = searchRequestService.update(searchRequest);
        SearchResult result = new SearchResult();
        result.setSearchRequest(searchRequest);
        return result;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
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
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetSearchIndex() {
        log.info("resetSearchIndex");
        searchResultService.resetSearchIndex();
    }
}
