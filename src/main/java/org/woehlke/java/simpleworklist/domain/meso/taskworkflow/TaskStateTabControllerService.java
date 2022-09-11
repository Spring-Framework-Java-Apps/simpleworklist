package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.Locale;

public interface TaskStateTabControllerService {

  String getTaskStatePage(
    TaskState taskState,
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

}
