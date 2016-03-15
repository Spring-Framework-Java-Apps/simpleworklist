package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 15.03.2016.
 */
public enum Language {

    @Enumerated
    EN,

    @Enumerated
    DE;

    public int getId(){
        return this.ordinal();
    }

    public String getValue(){
        return this.name();
    }

    public String getCode(){
        return "enum."+this.getClass().getSimpleName().toLowerCase() + "." + this.name().toLowerCase();
    }

    public static List<Language> list() {
        return Arrays.asList(values());
    }
}
