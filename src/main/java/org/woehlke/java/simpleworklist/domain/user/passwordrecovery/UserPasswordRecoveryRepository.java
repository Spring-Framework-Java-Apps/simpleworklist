package org.woehlke.java.simpleworklist.domain.user.passwordrecovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRecoveryRepository extends JpaRepository<UserPasswordRecovery, Long> {

    UserPasswordRecovery findByToken(String token);

    UserPasswordRecovery findByEmail(String email);
}
