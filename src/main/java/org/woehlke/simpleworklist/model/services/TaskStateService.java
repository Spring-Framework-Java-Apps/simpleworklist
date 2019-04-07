package org.woehlke.simpleworklist.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;

/**
 * Created by tw on 21.02.16.
 */
public interface TaskStateService {

    Page<Task> getFocus(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getInbox(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getToday(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getNext(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getWaiting(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getScheduled(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getSomeday(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getCompleted(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getTrash(UserAccount thisUser, Context context, Pageable request);

}
