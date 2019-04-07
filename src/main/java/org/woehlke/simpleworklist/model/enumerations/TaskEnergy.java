package org.woehlke.simpleworklist.model.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskEnergy implements Serializable {

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
