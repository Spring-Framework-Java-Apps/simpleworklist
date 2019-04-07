package org.woehlke.simpleworklist.entities.services;

import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.NewContextForm;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface ContextService {

    List<Context> getAllForUser(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

    void createNewContext(NewContextForm newContext, UserAccount user);

    void updateContext(NewContextForm editContext, UserAccount user, long contextId);

    boolean delete(Context context);

    boolean contextHasItems(Context context);
}