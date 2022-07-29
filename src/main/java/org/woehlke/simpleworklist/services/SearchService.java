package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.search.request.SearchRequest;
import org.woehlke.simpleworklist.domain.search.result.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(SearchRequest searchRequest);

    SearchResult search(String searchterm, Context context);

    void resetSearchIndex();

}
