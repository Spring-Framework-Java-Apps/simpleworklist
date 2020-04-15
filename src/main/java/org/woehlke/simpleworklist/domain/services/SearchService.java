package org.woehlke.simpleworklist.domain.services;

import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.search.SearchRequest;
import org.woehlke.simpleworklist.domain.search.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(SearchRequest searchRequest);

    SearchResult search(String searchterm, Context context);

    void resetSearchIndex();

}
