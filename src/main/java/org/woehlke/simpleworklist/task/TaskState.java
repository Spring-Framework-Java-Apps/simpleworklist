package org.woehlke.simpleworklist.task;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 21.02.16.
 */
public enum TaskState implements Serializable {


    INBOX("fas fa-inbox"),
    TODAY("fas fa-clock"),
    NEXT("fas fa-cogs"),
    WAITING("fas fa-hourglass-half"),
    SCHEDULED("fas fa-calendar-alt"),
    SOMEDAY("fas fa-road"),
    FOCUS("fas fa-star"),
    COMPLETED("fas fa-check-square"),
    TRASH("fas fa-trash-alt"),
    DELETED("fas fa-trash-alt");

    TaskState(final String icon) {
        this.icon=icon;
    }

    public int getId(){
        return this.ordinal();
    }

    public String getValue(){
        return this.name();
    }

    public String getCode() {
        return "layout.page."+this.name().toLowerCase();
    }

    public String getUrl() {
        return "redirect:/taskstate/"+this.name().toLowerCase();
    }

    public static List<TaskState> list() {
        return Arrays.asList(values());
    }

    public String getIcon() {
        return icon;
    }

    private String icon;

    private static final long serialVersionUID = 0L;
}
