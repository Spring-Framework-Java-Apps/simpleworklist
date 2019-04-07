package org.woehlke.simpleworklist.model.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;

public enum UserRole implements Serializable {

    @Enumerated
    ROLE_USER,

    @Enumerated
    ROLE_ADMIN
}
