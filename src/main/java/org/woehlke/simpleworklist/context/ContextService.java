package org.woehlke.simpleworklist.context;

import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface ContextService {

    List<Context> getAllForUser(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

    Context createNewContext(NewContextForm newContext, UserAccount user);

    Context updateContext(Context context);

    boolean delete(Context context);

    boolean contextHasItems(Context context);
}
