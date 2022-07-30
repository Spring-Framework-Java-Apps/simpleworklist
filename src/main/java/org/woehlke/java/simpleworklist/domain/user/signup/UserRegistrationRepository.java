package org.woehlke.java.simpleworklist.domain.user.signup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

    UserRegistration findByToken(String token);

    UserRegistration findByEmail(String email);
}
