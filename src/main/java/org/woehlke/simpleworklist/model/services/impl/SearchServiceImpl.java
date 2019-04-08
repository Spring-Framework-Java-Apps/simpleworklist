package org.woehlke.simpleworklist.model.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.SearchResult;
import org.woehlke.simpleworklist.model.dao.SearchDao;
import org.woehlke.simpleworklist.model.services.SearchService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 14.02.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final SearchDao searchDao;

    @Autowired
    public SearchServiceImpl(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SearchResult search(String searchterm, UserAccount userAccount) {
        SearchResult searchResult = searchDao.search(searchterm, userAccount);
        return searchResult;
    }

    @Override
    public void resetSearchIndex() {
        LOGGER.info("resetSearchIndex");
        searchDao.resetSearchIndex();
    }
}
