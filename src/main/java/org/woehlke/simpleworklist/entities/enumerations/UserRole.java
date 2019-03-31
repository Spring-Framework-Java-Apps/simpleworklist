package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

public enum UserRole {

    @Enumerated
    ROLE_USER,

    @Enumerated
    ROLE_ADMIN
}
