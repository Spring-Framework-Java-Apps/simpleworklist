package org.woehlke.java.simpleworklist.domain.db.search.result;

import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;

public interface SearchResultService {

    void resetSearchIndex();

    SearchResult add(SearchResult searchResult);
    SearchResult update(SearchResult searchResult);

}
