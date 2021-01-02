package org.woehlke.simpleworklist.user.domain.resetpassword;

import javax.persistence.Enumerated;
import java.io.Serializable;

public enum UserPasswordRecoveryStatus implements Serializable {

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
