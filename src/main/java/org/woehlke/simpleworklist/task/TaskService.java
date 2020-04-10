package org.woehlke.simpleworklist.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;

public interface TaskService {

    Task findOne(long taskId);
    boolean projectHasNoTasks(Project project);

    Page<Task> findbyTaskstate(TaskState taskState, Context context, Pageable request);

    Page<Task> findByProject(Project thisProject, Pageable request);
    Page<Task> findByRootProject(Context context, Pageable request);

    long getMaxOrderIdTaskState(TaskState taskState, Context context);
    long getMaxOrderIdRootProject(Context context);
    long getMaxOrderIdProject(Project project, Context context);



    Task addToInbox(Task task);
    Task addToRootProject(Task task);
    Task addToProject(Task task);

    Task updatedViaTaskstate(Task task);
    Task updatedViaProject(Task task);

    Task moveTaskToRootProject(Task task);
    Task moveTaskToAnotherProject(Task task, Project project);
    void moveAllCompletedToTrash(Context context);
    void emptyTrash(Context context);

    void moveOrderIdTaskState(Task sourceTask, Task destinationTask);
    void moveOrderIdProject(Task sourceTask, Task destinationTask);
    void moveOrderIdRootProject(Task sourceTask, Task destinationTask);
}
