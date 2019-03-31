package org.woehlke.simpleworklist.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.woehlke.simpleworklist.application.ContextRefreshedListener;
import org.woehlke.simpleworklist.services.SearchService;


/**
 * Created by tw on 14.02.16.
 */
@Component
public class ContextRefreshedListenerImpl implements ContextRefreshedListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedListenerImpl.class);

    @Autowired
    private SearchService searchService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("onApplicationEvent: "+event.toString());
        LOGGER.info("----------------------------------------------------");
        searchService.resetSearchIndex();
        LOGGER.info("----------------------------------------------------");
    }
}
