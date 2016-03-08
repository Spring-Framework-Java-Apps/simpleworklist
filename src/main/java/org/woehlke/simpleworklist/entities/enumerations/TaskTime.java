package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskTime {

    @Enumerated
    MIN5("5 min"),

    @Enumerated
    MIN10("10 min"),

    @Enumerated
    MIN15("15 min"),

    @Enumerated
    MIN30("30 min"),

    @Enumerated
    MIN45("45 min"),

    @Enumerated
    HOUR1("1 hour"),

    @Enumerated
    HOUR2("2 hours"),

    @Enumerated
    HOUR3("3 hours"),

    @Enumerated
    HOUR4("4 hours"),

    @Enumerated
    HOUR6("6 hours"),

    @Enumerated
    HOUR8("8 hours"),

    @Enumerated
    MORE("more"),

    @Enumerated
    NONE("none");

    private String s;

    private TaskTime(String s){
        this.s = s;
    }

    public String getValue(){
        return this.name();
    }

    public String getLabel(){
        return s;
    }

    public String toString(){
        return s;
    }
}
