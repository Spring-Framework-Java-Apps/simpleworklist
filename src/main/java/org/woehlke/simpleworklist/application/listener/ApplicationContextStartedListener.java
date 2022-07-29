package org.woehlke.simpleworklist.application.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.woehlke.simpleworklist.domain.search.SearchService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tw on 14.02.16.
 */
@Component
public class ApplicationContextStartedListener implements ApplicationListener<ContextStartedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextStartedListener.class);

    private final SearchService searchService;

    @Autowired
    public ApplicationContextStartedListener(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("   Spring Context Started Listener                  ");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("   onApplicationEvent: "+event.toString());
        LOGGER.info("----------------------------------------------------");
        searchService.resetSearchIndex();
        LOGGER.info("----------------------------------------------------");
    }

}
