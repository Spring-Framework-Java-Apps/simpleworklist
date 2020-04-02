package org.woehlke.simpleworklist.search;

import org.woehlke.simpleworklist.oodm.entities.UserAccount;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchDao {

    SearchResult search(String searchterm, UserAccount userAccount);

    void resetSearchIndex();

}
