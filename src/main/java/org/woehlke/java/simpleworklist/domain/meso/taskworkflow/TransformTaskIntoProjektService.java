package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

public interface TransformTaskIntoProjektService {

    String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
