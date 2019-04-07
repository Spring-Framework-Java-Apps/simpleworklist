package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface TaskMoveService {

    void deleteAllCompleted(Context context, UserAccount thisUser);

    void emptyTrash(UserAccount userAccount, Context context);
}
