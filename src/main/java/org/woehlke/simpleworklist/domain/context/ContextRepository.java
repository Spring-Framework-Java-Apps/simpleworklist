package org.woehlke.simpleworklist.domain.context;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
@SuppressWarnings("Deprecation")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {

    //TODO: #251 change List<Context> to Page<Context>
    @Deprecated
    List<Context> findByUserAccount(UserAccount user);
    Page<Context> findByUserAccount(UserAccount user, Pageable pageRequest);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

}
