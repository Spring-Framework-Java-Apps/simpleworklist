package org.woehlke.java.simpleworklist.domain.user.accountpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountPasswordRepository extends JpaRepository<UserAccountPassword, Long> {
}
