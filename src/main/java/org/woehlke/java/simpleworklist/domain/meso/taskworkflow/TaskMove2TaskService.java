package org.woehlke.java.simpleworklist.domain.meso.taskworkflow;

import org.woehlke.java.simpleworklist.domain.db.data.Task;

public interface TaskMove2TaskService {

  void moveTaskToTaskAndChangeTaskOrderInTaskstate(Task sourceTask, Task destinationTask);

}
