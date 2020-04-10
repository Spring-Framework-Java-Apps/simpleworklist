package org.woehlke.simpleworklist.search;

import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.user.account.UserAccount;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {

    SearchResult search(SearchRequest searchRequest);

    SearchResult search(String searchterm, Context context);

    void resetSearchIndex();

}
