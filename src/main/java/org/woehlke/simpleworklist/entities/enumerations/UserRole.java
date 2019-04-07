package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;

public enum UserRole implements Serializable {

    @Enumerated
    ROLE_USER,

    @Enumerated
    ROLE_ADMIN
}
