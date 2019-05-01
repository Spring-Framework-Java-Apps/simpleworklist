package org.woehlke.simpleworklist.oodm.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;

public enum UserRole implements Serializable {

    @Enumerated
    ROLE_USER,

    @Enumerated
    ROLE_ADMIN;

    private static final long serialVersionUID = 0L;
}
