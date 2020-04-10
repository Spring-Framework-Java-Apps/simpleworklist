package org.woehlke.simpleworklist.task;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.session.UserSessionBean;

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

    String transformTaskIntoProjectGet(Task task);
}
