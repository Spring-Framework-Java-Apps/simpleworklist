package org.woehlke.java.simpleworklist.domain.db.search.request;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.java.simpleworklist.domain.db.search.SearchRequest;

import java.util.UUID;

@Log
@Service
public class SearchRequestServiceImpl implements SearchRequestService {

    private final SearchRequestRepository searchRequestRepository;

    @Autowired
    public SearchRequestServiceImpl(SearchRequestRepository searchRequestRepository) {
        this.searchRequestRepository = searchRequestRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public SearchRequest add(SearchRequest searchRequest) {
        searchRequest.setUuid(UUID.randomUUID());
        return searchRequestRepository.saveAndFlush(searchRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public SearchRequest update(SearchRequest searchRequest) {
        return searchRequestRepository.saveAndFlush(searchRequest);
    }
}
