package org.woehlke.simpleworklist.user.domain.resetpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRecoveryRepository extends JpaRepository<UserPasswordRecovery, Long> {

    UserPasswordRecovery findByToken(String token);

    UserPasswordRecovery findByEmail(String email);
}
