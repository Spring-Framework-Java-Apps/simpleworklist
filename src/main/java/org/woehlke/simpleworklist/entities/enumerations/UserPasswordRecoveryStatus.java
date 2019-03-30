package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

public enum UserPasswordRecoveryStatus {

    @Enumerated
    PASSWORD_RECOVERY_SAVED_EMAIL,

    @Enumerated
    PASSWORD_RECOVERY_SENT_EMAIL,

    @Enumerated
    PASSWORD_RECOVERY_CLICKED_IN_MAIL,

    @Enumerated
    PASSWORD_RECOVERY_STORED_CHANGED
}
