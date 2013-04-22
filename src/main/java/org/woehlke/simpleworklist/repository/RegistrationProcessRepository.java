package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.RegistrationProcess;

public interface RegistrationProcessRepository extends JpaRepository<RegistrationProcess, Long> {

	RegistrationProcess findByToken(String confirmId);
	RegistrationProcess findByEmail(String email);
}
