package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

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
