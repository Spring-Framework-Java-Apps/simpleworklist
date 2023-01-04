package org.woehlke.java.simpleworklist.domain.db.user.account;

import jakarta.persistence.Enumerated;
import java.io.Serializable;

public enum UserRole implements Serializable {

    @Enumerated
    ROLE_USER,

    @Enumerated
    ROLE_ADMIN;

    private static final long serialVersionUID = 0L;
}
