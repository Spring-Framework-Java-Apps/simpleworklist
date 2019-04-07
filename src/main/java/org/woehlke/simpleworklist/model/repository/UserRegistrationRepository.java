package org.woehlke.simpleworklist.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.model.entities.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

    UserRegistration findByToken(String token);

    UserRegistration findByEmail(String email);
}
