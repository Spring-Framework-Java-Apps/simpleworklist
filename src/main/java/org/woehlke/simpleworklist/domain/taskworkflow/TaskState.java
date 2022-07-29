package org.woehlke.simpleworklist.domain.taskworkflow;

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

    private String icon;

    TaskState(final String icon){
        this.icon=icon;
    }

    public String getIcon(){
        return icon;
    }

    public int getId(){
        return this.ordinal();
    }

    public String getValue(){
        return this.name();
    }

    public String getType(){
        return this.name().toLowerCase();
    }

    public String getCode(){
        return "layout.page."+this.name().toLowerCase();
    }

    public String getUrlPath(){
        return "/taskstate/"+this.name().toLowerCase();
    }

    public String getUrl(){
        return "redirect:/taskstate/"+this.name().toLowerCase();
    }

    public String getTemplate(){
        return "taskstate/"+this.name().toLowerCase();
    }

    public static List<TaskState> list() {
        return Arrays.asList(values());
    }

    private static final long serialVersionUID = 0L;
}
