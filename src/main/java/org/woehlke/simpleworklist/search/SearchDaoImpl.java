package org.woehlke.simpleworklist.search;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.user.account.UserAccount;

import javax.persistence.*;

/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Repository
public class SearchDaoImpl implements SearchDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public SearchResult search(String searchterm, UserAccount userAccount) {
        log.info("search");
        return new SearchResult();
    }

    @Override
    public void resetSearchIndex() {
        log.info("resetSearchIndex");
    }
}
