package org.woehlke.simpleworklist.task;

import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;

public interface TaskMoveService {

    Task moveTaskToRootProject(Task task);

    Task moveTaskToAnotherProject(Task task, Project project);

    void moveAllCompletedToTrash(Context context);

    void emptyTrash(Context context);

    long getMaxOrderIdTaskState(TaskState taskState, Context context);

    long getMaxOrderIdRootProject(Context context);

    long getMaxOrderIdProject(Project project, Context context);

    void moveOrderIdTaskState(Task sourceTask, Task destinationTask);

    void moveOrderIdProject(Task sourceTask, Task destinationTask);

    void moveOrderIdRootProject(Task sourceTask, Task destinationTask);

}
