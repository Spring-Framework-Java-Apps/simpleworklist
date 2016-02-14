package org.woehlke.simpleworklist.repository;

import org.woehlke.simpleworklist.model.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchDao {

    SearchResult search(String searchterm);

    void resetSearchIndex();

}
