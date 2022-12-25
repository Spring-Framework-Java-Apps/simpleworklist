package org.woehlke.java.simpleworklist.domain.meso.task;

import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;

public interface TaskMoveService {

  Task moveTaskToAnotherProject(Task task, Project project);

  Task moveTaskToRootProject(Task task);

  Task moveTaskToInbox(Task task);

  Task moveTaskToToday(Task task);

  Task moveTaskToNext(Task task);

  Task moveTaskToWaiting(Task task);

  Task moveTaskToScheduled(Task task);

  Task moveTaskToSomeday(Task task);

  Task moveTaskToFocus(Task task);

  Task moveTaskToCompleted(Task task);

  Task moveTaskToTrash(Task task);

  void moveAllCompletedToTrash(Context context);

  void emptyTrash(Context context);

}
