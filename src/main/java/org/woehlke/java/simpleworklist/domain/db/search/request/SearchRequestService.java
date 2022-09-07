package org.woehlke.java.simpleworklist.domain.db.search.request;

import org.woehlke.java.simpleworklist.domain.db.search.SearchRequest;

public interface SearchRequestService {

    SearchRequest add(SearchRequest searchRequest);
    SearchRequest update(SearchRequest searchRequest);

}
