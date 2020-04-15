package org.woehlke.simpleworklist.search.services;

import org.woehlke.simpleworklist.search.SearchResult;

public interface SearchResultService {

    void resetSearchIndex();

    SearchResult add(SearchResult searchResult);
    SearchResult update(SearchResult searchResult);

}
