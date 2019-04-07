package org.woehlke.simpleworklist.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.model.entities.UserPasswordRecovery;

@Repository
public interface UserPasswordRecoveryRepository extends JpaRepository<UserPasswordRecovery, Long> {

    UserPasswordRecovery findByToken(String token);

    UserPasswordRecovery findByEmail(String email);
}
