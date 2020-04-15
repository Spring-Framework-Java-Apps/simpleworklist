package org.woehlke.simpleworklist.services;

import org.springframework.ui.Model;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

public interface TaskProjektService {

    String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
