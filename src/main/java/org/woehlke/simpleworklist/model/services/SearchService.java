package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.model.entities.UserAccount;
import org.woehlke.simpleworklist.model.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(String searchterm, UserAccount userAccount);

    void resetSearchIndex();

}
