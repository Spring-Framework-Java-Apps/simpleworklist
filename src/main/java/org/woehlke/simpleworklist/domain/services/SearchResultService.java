package org.woehlke.simpleworklist.domain.services;

import org.woehlke.simpleworklist.domain.search.SearchResult;

public interface SearchResultService {

    void resetSearchIndex();

    SearchResult add(SearchResult searchResult);
    SearchResult update(SearchResult searchResult);

}
