package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

public interface TaskMoveService {

    void deleteAllCompleted(Context context, UserAccount thisUser);

    void emptyTrash(UserAccount userAccount, Context context);
}
