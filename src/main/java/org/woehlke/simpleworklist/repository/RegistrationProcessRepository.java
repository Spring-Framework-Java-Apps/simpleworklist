package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.enumerations.RegistrationProcessType;

public interface RegistrationProcessRepository extends JpaRepository<RegistrationProcess, Long> {

    RegistrationProcess findByToken(String confirmId);

    RegistrationProcess findByEmailAndRegistrationProcessType(String email,RegistrationProcessType registrationProcessType);
}
