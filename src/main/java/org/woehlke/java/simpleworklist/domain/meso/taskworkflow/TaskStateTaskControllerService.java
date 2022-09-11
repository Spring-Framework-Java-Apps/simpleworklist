package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.TaskState;

public interface TaskStateTaskControllerService {

  Task updatedViaTaskstate(Task persistentTask);

  long getMaxOrderIdTaskState(TaskState completed, Context context);

  Task addToInbox(Task task);

}
