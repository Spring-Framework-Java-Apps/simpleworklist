package org.woehlke.simpleworklist.application.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.woehlke.simpleworklist.search.services.SearchService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 14.02.16.
 */
@Component
public class ContextStartedListener implements ApplicationListener<ContextStartedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextStartedListener.class);

    private final SearchService searchService;

    @Autowired
    public ContextStartedListener(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("onApplicationEvent and resetSearchIndex "+event.toString());
        LOGGER.info("----------------------------------------------------");
        searchService.resetSearchIndex();
        LOGGER.info("----------------------------------------------------");
    }

}
