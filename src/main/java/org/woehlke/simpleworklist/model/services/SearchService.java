package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(String searchterm, UserAccount userAccount);

    void resetSearchIndex();

}
