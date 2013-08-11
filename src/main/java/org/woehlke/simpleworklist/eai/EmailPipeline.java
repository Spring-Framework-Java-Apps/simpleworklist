package org.woehlke.simpleworklist.eai;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface EmailPipeline {

	void sendMail(RegistrationProcess o);

    void sendPasswordResetEmail(RegistrationProcess o);
}
