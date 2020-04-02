package org.woehlke.simpleworklist.search;

import org.woehlke.simpleworklist.user.account.UserAccount;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(String searchterm, UserAccount userAccount);

    void resetSearchIndex();

}
