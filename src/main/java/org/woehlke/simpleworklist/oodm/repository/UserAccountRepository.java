package org.woehlke.simpleworklist.oodm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount findByUserEmailAndUserPassword(String userEmail, String userPassword);

    UserAccount findByUserEmail(String userEmail);

}
