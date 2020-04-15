package org.woehlke.simpleworklist.task.services;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskState;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import java.util.Locale;

public interface TaskStateControllerService {

    String getTaskStatePage(
        TaskState taskState,
        Context context,
        Pageable pageRequest,
        UserSessionBean userSession,
        Locale locale,
        Model model
    );

    void moveTaskToTaskAndChangeTaskOrderInTaskstate(Task sourceTask, Task destinationTask);
}
