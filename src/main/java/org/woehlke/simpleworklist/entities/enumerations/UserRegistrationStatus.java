package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;

public enum UserRegistrationStatus implements Serializable {

    @Enumerated
    REGISTRATION_SAVED_EMAIL,

    @Enumerated
    REGISTRATION_SENT_MAIL,

    @Enumerated
    REGISTRATION_CLICKED_IN_MAIL,

    @Enumerated
    REGISTRATION_ACCOUNT_CREATED

}
