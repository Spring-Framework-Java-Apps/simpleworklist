package org.woehlke.java.simpleworklist.domain.db.data.context;

import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.List;
import java.util.Optional;

/**
 * Created by tw on 13.03.16.
 */
public interface ContextService {

    //TODO: rename to findByUser
    List<Context> getAllForUser(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

    //TODO: rename to add
    Context createNewContext(NewContextForm newContext, UserAccount user);

    //TODO: rename to update
    Context updateContext(Context context);

    boolean delete(Context context);

    boolean contextHasItems(Context context);

    Optional<Context> getContextFor(UserSessionBean userSession);
}
