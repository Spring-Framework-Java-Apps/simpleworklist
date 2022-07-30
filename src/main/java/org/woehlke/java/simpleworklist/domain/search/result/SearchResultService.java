package org.woehlke.java.simpleworklist.domain.search.result;

public interface SearchResultService {

    void resetSearchIndex();

    SearchResult add(SearchResult searchResult);
    SearchResult update(SearchResult searchResult);

}
