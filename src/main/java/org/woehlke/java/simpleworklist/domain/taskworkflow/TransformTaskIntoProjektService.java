package org.woehlke.java.simpleworklist.domain.taskworkflow;

import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.task.Task;
import org.woehlke.java.simpleworklist.test.application.session.UserSessionBean;

public interface TransformTaskIntoProjektService {

    String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
