package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

	boolean isRetryAndMaximumNumberOfRetries(String email);
	void checkIfResponseIsInTime(String email);
	void startSecondOptIn(String email);
	RegistrationProcess findByToken(String confirmId);
	void deleteAll();
    int getNumberOfAll();
    void registratorClickedInEmail(RegistrationProcess o);
    void userCreated(RegistrationProcess registrationProcess);
    void sendPasswordResetTo(String email);
    void usersPasswordChangeClickedInEmail(RegistrationProcess registrationProcess);
    void usersPasswordChanged(RegistrationProcess o);
}
