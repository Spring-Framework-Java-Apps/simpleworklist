package org.woehlke.simpleworklist.oodm.services;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.NewContextForm;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface ContextService {

    List<Context> getAllForUser(UserAccount user);

    Context findByIdAndUserAccount(long newContextId, UserAccount userAccount);

    void createNewContext(NewContextForm newContext, UserAccount user);

    void updateContext(Context context);

    boolean delete(Context context);

    boolean contextHasItems(Context context);
}
