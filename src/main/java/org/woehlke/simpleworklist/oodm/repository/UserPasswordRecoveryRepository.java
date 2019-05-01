package org.woehlke.simpleworklist.oodm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.oodm.entities.UserPasswordRecovery;

@Repository
public interface UserPasswordRecoveryRepository extends JpaRepository<UserPasswordRecovery, Long> {

    UserPasswordRecovery findByToken(String token);

    UserPasswordRecovery findByEmail(String email);
}
