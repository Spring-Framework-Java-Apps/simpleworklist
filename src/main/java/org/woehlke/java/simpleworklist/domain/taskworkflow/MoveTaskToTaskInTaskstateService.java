package org.woehlke.java.simpleworklist.domain.taskworkflow;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.context.Context;
import org.woehlke.java.simpleworklist.domain.task.Task;
import org.woehlke.java.simpleworklist.application.session.UserSessionBean;

import java.util.Locale;

public interface MoveTaskToTaskInTaskstateService {

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
