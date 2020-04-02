package org.woehlke.simpleworklist.user.resetpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.user.resetpassword.UserPasswordRecovery;

@Repository
public interface UserPasswordRecoveryRepository extends JpaRepository<UserPasswordRecovery, Long> {

    UserPasswordRecovery findByToken(String token);

    UserPasswordRecovery findByEmail(String email);
}
