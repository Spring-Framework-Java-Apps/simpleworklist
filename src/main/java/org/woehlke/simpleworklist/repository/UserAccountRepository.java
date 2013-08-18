package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserEmailAndUserPassword(String userEmail, String userPassword);

    UserAccount findByUserEmail(String userEmail);

}
