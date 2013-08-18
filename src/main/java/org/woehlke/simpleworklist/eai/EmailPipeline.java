package org.woehlke.simpleworklist.eai;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface EmailPipeline {

    void sendEmailToRegisterNewUser(RegistrationProcess o);

    void sendEmailForPasswordReset(RegistrationProcess o);
}
