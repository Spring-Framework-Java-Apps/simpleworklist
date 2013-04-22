package org.woehlke.simpleworklist.entities;

import javax.persistence.Enumerated;

public enum RegistrationProcessStatus {

	@Enumerated
	SAVED_EMAIL,
	
	@Enumerated
	SENT_MAIL,
	
	@Enumerated
	ACCOUNT_CREATED
}
