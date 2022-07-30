package org.woehlke.java.simpleworklist.test.application.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.woehlke.java.simpleworklist.domain.search.SearchService;


/**
 * Created by tw on 14.02.16.
 */
@Component
public class ApplicationContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextRefreshedListener.class);

    private final SearchService searchService;

    @Autowired
    public ApplicationContextRefreshedListener(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("   Spring Context Refreshed Listener                ");
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("   onApplicationEvent: "+event.toString());
        LOGGER.info("----------------------------------------------------");
        searchService.resetSearchIndex();
        LOGGER.info("----------------------------------------------------");
    }
}
