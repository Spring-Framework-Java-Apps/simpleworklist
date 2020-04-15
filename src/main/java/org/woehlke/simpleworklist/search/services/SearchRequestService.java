package org.woehlke.simpleworklist.search.services;

import org.woehlke.simpleworklist.search.SearchRequest;

public interface SearchRequestService {

    SearchRequest add(SearchRequest searchRequest);
    SearchRequest update(SearchRequest searchRequest);

}
