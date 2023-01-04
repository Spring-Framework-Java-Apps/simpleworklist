package org.woehlke.java.simpleworklist.domain.db.user.signup;

import jakarta.persistence.Enumerated;
import java.io.Serializable;

public enum UserAccountRegistrationStatus implements Serializable {

    @Enumerated
    REGISTRATION_SAVED_EMAIL,

    @Enumerated
    REGISTRATION_SENT_MAIL,

    @Enumerated
    REGISTRATION_CLICKED_IN_MAIL,

    @Enumerated
    REGISTRATION_ACCOUNT_CREATED;

    private static final long serialVersionUID = 0L;

}
