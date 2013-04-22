package org.woehlke.simpleworklist.eai;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface EmailPipeline {

	public void sendMail(RegistrationProcess o);
}
