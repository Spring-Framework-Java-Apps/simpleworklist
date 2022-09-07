package org.woehlke.java.simpleworklist.domain.db.user.passwordrecovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPasswordRecovery;

@Repository
public interface UserAccountPasswordRecoveryRepository extends JpaRepository<UserAccountPasswordRecovery, Long> {

    UserAccountPasswordRecovery findByToken(String token);

    UserAccountPasswordRecovery findByEmail(String email);
}
