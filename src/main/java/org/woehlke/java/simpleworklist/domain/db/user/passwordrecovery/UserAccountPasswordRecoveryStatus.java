package org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery;

import jakarta.persistence.Enumerated;
import java.io.Serializable;

public enum UserAccountPasswordRecoveryStatus implements Serializable {

    @Enumerated
    PASSWORD_RECOVERY_SAVED_EMAIL,

    @Enumerated
    PASSWORD_RECOVERY_SENT_EMAIL,

    @Enumerated
    PASSWORD_RECOVERY_CLICKED_IN_MAIL,

    @Enumerated
    PASSWORD_RECOVERY_STORED_CHANGED;

    private static final long serialVersionUID = 0L;
}
