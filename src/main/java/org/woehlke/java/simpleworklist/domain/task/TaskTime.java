package org.woehlke.java.simpleworklist.domain.task;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskTime implements Serializable {

    @Enumerated
    MIN5,

    @Enumerated
    MIN10,

    @Enumerated
    MIN15,

    @Enumerated
    MIN30,

    @Enumerated
    MIN45,

    @Enumerated
    HOUR1,

    @Enumerated
    HOUR2,

    @Enumerated
    HOUR3,

    @Enumerated
    HOUR4,

    @Enumerated
    HOUR6,

    @Enumerated
    HOUR8,

    @Enumerated
    MORE,

    @Enumerated
    NONE;

    public int getId(){
        return this.ordinal();
    }

    public String getValue(){
        return this.name();
    }

    public String getCode(){
        return "enum."+this.getClass().getSimpleName().toLowerCase() + "." + this.name().toLowerCase();
    }

    public static List<TaskTime> list() {
        return Arrays.asList(values());
    }

    private static final long serialVersionUID = 0L;
}
