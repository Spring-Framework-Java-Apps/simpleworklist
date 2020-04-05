package org.woehlke.simpleworklist.task;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.user.UserSessionBean;

import java.util.Locale;

public interface TaskControllerService {

    String getView(
        Task task,
        String back
    );

    String getTaskStatePage(
        TaskState taskState,
        Page<Task> taskPage,
        UserSessionBean userSession,
        Locale locale,
        Model model
    );
}
