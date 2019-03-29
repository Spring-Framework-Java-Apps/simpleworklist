package org.woehlke.simpleworklist.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.SearchResult;
import org.woehlke.simpleworklist.repository.SearchDao;
import org.woehlke.simpleworklist.services.SearchService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 14.02.16.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private SearchDao searchDao;

    @Override
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
