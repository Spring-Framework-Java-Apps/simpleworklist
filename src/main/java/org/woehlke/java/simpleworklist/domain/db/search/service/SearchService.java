package org.woehlke.java.simpleworklist.domain.db.search.service;

import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.search.SearchRequest;
import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(SearchRequest searchRequest);

    SearchResult search(String searchterm, Context context);

    void resetSearchIndex();

}
