package org.woehlke.simpleworklist.taskstate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.task.Task;

/**
 * Created by tw on 21.02.16.
 */
public interface TaskStateService {

    Page<Task> getFocus(Context context, Pageable request);

    Page<Task> getInbox(Context context, Pageable request);

    Page<Task> getToday(Context context, Pageable request);

    Page<Task> getNext(Context context, Pageable request);

    Page<Task> getWaiting(Context context, Pageable request);

    Page<Task> getScheduled(Context context, Pageable request);

    Page<Task> getSomeday(Context context, Pageable request);

    Page<Task> getCompleted(Context context, Pageable request);

    Page<Task> getTrash(Context context, Pageable request);

}
