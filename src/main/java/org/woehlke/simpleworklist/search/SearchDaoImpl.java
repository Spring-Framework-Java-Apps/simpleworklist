package org.woehlke.simpleworklist.search;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

import javax.persistence.*;

/**
 * Created by tw on 14.02.16.
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public SearchResult search(String searchterm, UserAccount userAccount) {
        return new SearchResult();
    }

    @Override
    public void resetSearchIndex() {
    }
}
