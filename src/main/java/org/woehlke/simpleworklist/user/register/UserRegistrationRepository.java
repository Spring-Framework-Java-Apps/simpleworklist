package org.woehlke.simpleworklist.user.register;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.user.register.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

    UserRegistration findByToken(String token);

    UserRegistration findByEmail(String email);
}
