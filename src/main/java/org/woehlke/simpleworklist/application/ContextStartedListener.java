package org.woehlke.simpleworklist.application;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

public interface ContextStartedListener extends ApplicationListener<ContextStartedEvent> {
}
