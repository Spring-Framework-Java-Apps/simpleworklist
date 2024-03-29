package org.woehlke.java.simpleworklist.domain.db.search.result;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.search.SearchResult;

import java.util.UUID;

@Log
@Service
public class SearchResultServiceImpl implements SearchResultService {

    private final SearchResultRepository searchResultRepository;

    @Autowired
    public SearchResultServiceImpl(SearchResultRepository searchResultRepository) {
        this.searchResultRepository = searchResultRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void resetSearchIndex() {
        this.searchResultRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public SearchResult add(SearchResult searchResult) {
        searchResult.setUuid(UUID.randomUUID());
        return searchResultRepository.saveAndFlush(searchResult);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public SearchResult update(SearchResult searchResult) {
        return searchResultRepository.saveAndFlush(searchResult);
    }
}
