package org.woehlke.java.simpleworklist.domain.db.user.accountpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccountPassword;

@Repository
public interface UserAccountPasswordRepository extends JpaRepository<UserAccountPassword, Long> {
}
