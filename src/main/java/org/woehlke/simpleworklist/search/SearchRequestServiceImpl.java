package org.woehlke.simpleworklist.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
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
        searchRequest.setUuid(UUID.randomUUID().toString());
        return searchRequestRepository.saveAndFlush(searchRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public SearchRequest update(SearchRequest searchRequest) {
        return searchRequestRepository.saveAndFlush(searchRequest);
    }
}
