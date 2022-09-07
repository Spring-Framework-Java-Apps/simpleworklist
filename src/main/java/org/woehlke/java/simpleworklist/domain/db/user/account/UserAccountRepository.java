package org.woehlke.java.simpleworklist.domain.db.user.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserEmailAndUserPassword(String userEmail, String userPassword);

    UserAccount findByUserEmail(String userEmail);

}
