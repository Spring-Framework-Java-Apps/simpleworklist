package org.woehlke.java.simpleworklist.domain.search;

import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.search.request.SearchRequest;
import org.woehlke.java.simpleworklist.domain.search.result.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(SearchRequest searchRequest);

    SearchResult search(String searchterm, Context context);

    void resetSearchIndex();

}
