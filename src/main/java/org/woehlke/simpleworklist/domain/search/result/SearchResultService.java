package org.woehlke.simpleworklist.domain.search.result;

import org.woehlke.simpleworklist.domain.search.result.SearchResult;

public interface SearchResultService {

    void resetSearchIndex();

    SearchResult add(SearchResult searchResult);
    SearchResult update(SearchResult searchResult);

}
