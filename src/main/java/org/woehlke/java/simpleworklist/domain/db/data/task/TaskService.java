package org.woehlke.java.simpleworklist.domain.db.data.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.TaskState;

import java.util.List;

public interface TaskService {

//    void moveAllCompletedToTrash(Context context);
//    void emptyTrash(Context context);

//    Task moveTaskToRootProject(Task task);
//    Task moveTaskToAnotherProject(Task task, Project project);

    Task addToInbox(Task task);
    Task addToProject(Task task);
    Task addToRootProject(Task task);

    Task updatedViaTaskstate(Task task);
    Task updatedViaProject(Task task);
    Task updatedViaProjectRoot(Task task);

    //TODO: rename to findById
    Task findOne(long taskId);
    Page<Task> findbyTaskstate(TaskState taskState, Context context, Pageable request);
    Page<Task> findByProject(Project thisProject, Pageable request);
    //TODO: rename to findByProjectRoot
    Page<Task> findByRootProject(Context context, Pageable request);

    boolean projectHasNoTasks(Project project);

    long getMaxOrderIdTaskState(TaskState taskState, Context context);
    long getMaxOrderIdProject(Project project, Context context);
    long getMaxOrderIdProjectRoot(Context context);

    /**
     * Before: sourceTask is dragged from above down to destinationTask, so sourceTask is above destinationTask.
     * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
     * and destinationTask are moved one position up; destinationTask is the next Task above sourceTask.
     * @param sourceTask Task
     * @param destinationTask Task
     */
    void moveTasksUpByTaskState(Task sourceTask, Task destinationTask);

    /**
     * Before: sourceTask is dragged from below up to destinationTask, so sourceTask is below destinationTask.
     * After: sourceTask is placed to the position of destinationTask, all tasks between old position of sourceTask
     * are moved one position down; destinationTask is the next Task below sourceTask.
     * @param sourceTask Task
     * @param destinationTask Task
     */
    void moveTasksDownByTaskState(Task sourceTask, Task destinationTask);

    void moveTasksUpByProjectRoot(Task sourceTask, Task destinationTask);
    void moveTasksDownByProjectRoot(Task sourceTask, Task destinationTask);

    void moveTasksUpByProject(Task sourceTask, Task destinationTask);
    void moveTasksDownByProject(Task sourceTask, Task destinationTask);

    Task saveAndFlush(Task task);

    void deleteAll(List<Task> taskListDeleted);

    void saveAll(List<Task> taskListChanged);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState completed, Context context);

    List<Task> findByTaskStateAndContext(TaskState trash, Context context);

}
