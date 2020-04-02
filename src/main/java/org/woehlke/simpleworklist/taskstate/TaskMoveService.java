package org.woehlke.simpleworklist.taskstate;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.oodm.enumerations.TaskState;

public interface TaskMoveService {

    Task moveTaskToRootProject(Task task);

    Task moveTaskToAnotherProject(Task task, Project project);

    Task moveTaskToInbox(Task task);

    Task moveTaskToToday(Task task);

    Task moveTaskToNext(Task task);

    Task moveTaskToWaiting(Task task);

    Task moveTaskToSomeday(Task task);

    Task moveTaskToFocus(Task task);

    Task moveTaskToCompleted(Task task);

    Task moveTaskToTrash(Task task);

    void deleteAllCompleted(Context context);

    void emptyTrash(Context context);

    long getMaxOrderIdTaskState(TaskState taskState, Context context);

    long getMaxOrderIdProject(Project project, Context context);

    void moveOrderIdTaskState(Task sourceTask, Task destinationTask);

    void moveOrderIdProject(Task sourceTask, Task destinationTask);

    void moveOrderIdRootProject(Task sourceTask, Task destinationTask);
}
