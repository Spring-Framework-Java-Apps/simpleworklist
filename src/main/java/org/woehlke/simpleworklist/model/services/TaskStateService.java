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

    Page<Task> getInbox(UserAccount thisUser, Pageable request);

    Page<Task> getToday(UserAccount thisUser, Pageable request);

    Page<Task> getNext(UserAccount thisUser, Pageable request);

    Page<Task> getWaiting(UserAccount thisUser, Pageable request);

    Page<Task> getScheduled(UserAccount thisUser, Pageable request);

    Page<Task> getSomeday(UserAccount thisUser, Pageable request);

    Page<Task> getCompleted(UserAccount thisUser, Pageable request);

    Page<Task> getTrash(UserAccount thisUser, Pageable request);

    void deleteAllCompleted(Context context, UserAccount thisUser);

    Page<Task> getFocus(Context context, UserAccount thisUser, Pageable request);

    Page<Task> getInbox(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getToday(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getNext(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getWaiting(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getScheduled(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getSomeday(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getCompleted(UserAccount thisUser, Context context, Pageable request);

    Page<Task> getTrash(UserAccount thisUser, Context context, Pageable request);

}
