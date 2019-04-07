package org.woehlke.simpleworklist.model.dao;

import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchDao {

    SearchResult search(String searchterm, UserAccount userAccount);

    void resetSearchIndex();

}
