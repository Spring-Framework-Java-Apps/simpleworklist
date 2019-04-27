package org.woehlke.simpleworklist.model.services;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
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

    void deleteAllCompleted(Context context, UserAccount thisUser);

    void emptyTrash(UserAccount userAccount, Context context);

    long getMaxOrderIdTaskState(TaskState taskState, Context context, UserAccount thisUser);

    long getMaxOrderIdProject(Project project, Context context, UserAccount userAccount);

    void moveOrderIdTaskState(TaskState taskState, Task sourceTask, Task destinationTask, Context context);

    void moveOrderIdProject(Project project, Task sourceTask, Task destinationTask, Context context);
}
