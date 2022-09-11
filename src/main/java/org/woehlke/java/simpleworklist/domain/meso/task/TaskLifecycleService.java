package org.woehlke.java.simpleworklist.domain.meso.task;

import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.task.TaskState;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

public interface TaskLifecycleService {

  Task addToInbox(Task task);
  Task addToProject(Task task);
  Task addToRootProject(Task task);

  Task updatedViaTaskstate(Task task);
  Task updatedViaProject(Task task);
  Task updatedViaProjectRoot(Task task);


  long getMaxOrderIdTaskState(TaskState completed, Context context);
  long getMaxOrderIdProject(Project project, Context context);
  long getMaxOrderIdProjectRoot(Context context);

  String transformTaskIntoProjectGet(Task task, UserSessionBean userSession, Model model);
}
