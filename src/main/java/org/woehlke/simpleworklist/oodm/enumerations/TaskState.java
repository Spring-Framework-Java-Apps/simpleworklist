package org.woehlke.simpleworklist.oodm.enumerations;

import io.micrometer.core.instrument.search.RequiredSearch;

import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by tw on 21.02.16.
 */
public enum TaskState implements Serializable {

    @Enumerated
    INBOX("fas fa-inbox"),

    @Enumerated
    TODAY("fas fa-clock"),

    @Enumerated
    NEXT("fas fa-cogs"),

    @Enumerated
    WAITING("fas fa-hourglass-half"),

    @Enumerated
    SCHEDULED("fas fa-calendar-alt"),

    @Enumerated
    SOMEDAY("fas fa-road"),

    @Enumerated
    FOCUS("fas fa-star"),

    @Enumerated
    COMPLETED("fas fa-check-square"),

    @Enumerated
    TRASH("fas fa-trash-alt");

    TaskState(final String icon) {
        this.icon=icon;
    }

    public String getCode() {
        return "layout.page."+this.name().toLowerCase();
    }

    public String getUrl() {
        return "/taskstate/"+this.name().toLowerCase();
    }

    public String getIcon() {
        return icon;
    }

    private String icon;

    private static final long serialVersionUID = 0L;
}
