package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskEnergy {

    @Enumerated
    LOW("low"),

    @Enumerated
    MEDIUM("medium"),

    @Enumerated
    HIGH("high"),

    @Enumerated
    NONE("none");

    private String s;

    private TaskEnergy(String s) {
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
