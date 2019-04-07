package org.woehlke.simpleworklist.entities.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    List<Context> findByUserAccount(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

}
