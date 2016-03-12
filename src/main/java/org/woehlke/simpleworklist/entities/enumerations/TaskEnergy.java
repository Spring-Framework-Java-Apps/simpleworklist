package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskEnergy {

    @Enumerated
    LOW,

    @Enumerated
    MEDIUM,

    @Enumerated
    HIGH,

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

    public static List<TaskEnergy> list() {
        return Arrays.asList(values());
    }
}
