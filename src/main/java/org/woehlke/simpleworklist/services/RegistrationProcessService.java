package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

	boolean isRetryAndMaximumNumberOfRetries(String email);
	void checkIfResponseIsInTime(String email);
	void startSecondOptIn(String email);
	RegistrationProcess findByToken(String confirmId);
	void deleteAll();

    int getNumberOfAll();
}
