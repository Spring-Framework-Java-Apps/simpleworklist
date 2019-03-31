package org.woehlke.simpleworklist.application;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public interface ContextRefreshedListener extends ApplicationListener<ContextRefreshedEvent> {
}
