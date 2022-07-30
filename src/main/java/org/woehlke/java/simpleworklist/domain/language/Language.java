package org.woehlke.java.simpleworklist.domain.language;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tw on 15.03.2016.
 */
public enum Language implements Serializable {

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

    private static final long serialVersionUID = 0L;
}
