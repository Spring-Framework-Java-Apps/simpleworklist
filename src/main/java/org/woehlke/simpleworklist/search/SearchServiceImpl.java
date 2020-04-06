package org.woehlke.simpleworklist.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 14.02.16.
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SearchServiceImpl implements SearchService {

    private final SearchDao searchDao;

    @Autowired
    public SearchServiceImpl(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SearchResult search(String searchterm, UserAccount userAccount) {
        log.info("search");
        SearchResult searchResult = searchDao.search(searchterm, userAccount);
        return searchResult;
    }

    @Override
    public void resetSearchIndex() {
        log.info("resetSearchIndex");
        searchDao.resetSearchIndex();
    }
}
