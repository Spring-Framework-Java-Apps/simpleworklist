package org.woehlke.simpleworklist.task;

import org.springframework.ui.Model;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

public interface TaskProjektService {

    String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
