package org.woehlke.java.simpleworklist.domain.user.signup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRegistrationRepository extends JpaRepository<UserAccountRegistration, Long> {

    UserAccountRegistration findByToken(String token);

    UserAccountRegistration findByEmail(String email);
}
