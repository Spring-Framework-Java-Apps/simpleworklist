package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessService {

	boolean isRetryAndMaximumNumberOfRetries(String email);
	void checkIfResponseIsInTime(String email);
	void sendEmailForVerification(String email);
    RegistrationProcess findByToken(String confirmId);
    void registratorClickedInEmail(RegistrationProcess o);
    void userCreated(RegistrationProcess registrationProcess);
    void sendPasswordResetTo(String email);
    void usersPasswordChangeClickedInEmail(RegistrationProcess registrationProcess);
    void usersPasswordChanged(RegistrationProcess o);
    void deleteAll();
    int getNumberOfAll();

    void sentEmailToRegisterNewUser(RegistrationProcess o);

    void sentEmailForPasswordReset(RegistrationProcess o);
}
