package org.woehlke.simpleworklist.eai;

import org.woehlke.simpleworklist.entities.UserPasswordRecovery;
import org.woehlke.simpleworklist.entities.UserRegistration;

public interface EmailPipeline {

    void sendEmailToRegisterNewUser(UserRegistration o);

    void sendEmailForPasswordReset(UserPasswordRecovery o);
}
