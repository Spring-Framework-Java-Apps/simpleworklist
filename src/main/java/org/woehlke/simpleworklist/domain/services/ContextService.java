package org.woehlke.simpleworklist.domain.services;

import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.domain.context.NewContextForm;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface ContextService {

    //TODO: #251 change List<Context> to Page<Context>
    //TODO: rename to findByUser
    List<Context> getAllForUser(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

    //TODO: rename to add
    Context createNewContext(NewContextForm newContext, UserAccount user);

    //TODO: rename to update
    Context updateContext(Context context);

    boolean delete(Context context);

    boolean contextHasItems(Context context);
}
