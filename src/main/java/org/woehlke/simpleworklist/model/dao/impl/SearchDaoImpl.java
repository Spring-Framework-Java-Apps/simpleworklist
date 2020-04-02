package org.woehlke.simpleworklist.model.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.SearchResult;
import org.woehlke.simpleworklist.model.dao.SearchDao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
