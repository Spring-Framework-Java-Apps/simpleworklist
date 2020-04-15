package org.woehlke.simpleworklist.task.services;

import org.springframework.ui.Model;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

public interface TaskProjektService {

    String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
