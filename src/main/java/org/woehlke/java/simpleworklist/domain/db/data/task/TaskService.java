package org.woehlke.java.simpleworklist.domain.db.data.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;

import java.util.List;

public interface TaskService {

    //TODO: rename to findById
    Task findOne(long taskId);

    List<Task> findByTaskStateInbox(Context context);
    List<Task> findByTaskStateToday(Context context);
    List<Task> findByTaskStateNext(Context context);
    List<Task> findByTaskStateWaiting(Context context);
    List<Task> findByTaskStateScheduled(Context context);
    List<Task> findByTaskStateSomeday(Context context);
    List<Task> findByFocus(Context context);
    List<Task> findByTaskStateCompleted(Context context);
    List<Task> findByTaskStateTrash(Context context);
    List<Task> findByTaskStateDeleted(Context context);
    List<Task> findByTaskStateProjects(Context context);

    Page<Task> findByTaskStateInbox(Context context, Pageable request);
    Page<Task> findByTaskStateToday(Context context, Pageable request);
    Page<Task> findByTaskStateNext(Context context, Pageable request);
    Page<Task> findByTaskStateWaiting(Context context, Pageable request);
    Page<Task> findByTaskStateScheduled(Context context, Pageable request);
    Page<Task> findByTaskStateSomeday(Context context, Pageable request);
    Page<Task> findByFocus(Context context, Pageable request);
    Page<Task> findByTaskStateCompleted(Context context, Pageable request);
    Page<Task> findByTaskStateTrash(Context context, Pageable request);
    Page<Task> findByTaskStateDeleted(Context context, Pageable request);
    Page<Task> findByTaskStateProjects(Context context, Pageable request);

    Page<Task> findByProject(Project thisProject, Pageable request);
    //TODO: rename to findByProjectRoot
    Page<Task> findByRootProject(Context context, Pageable request);

    boolean projectHasNoTasks(Project project);

    void moveTasksUpByProjectRoot(Task sourceTask, Task destinationTask);
    void moveTasksDownByProjectRoot(Task sourceTask, Task destinationTask);

    void moveTasksUpByProject(Task sourceTask, Task destinationTask);
    void moveTasksDownByProject(Task sourceTask, Task destinationTask);

    Task saveAndFlush(Task task);
    void saveAll(List<Task> taskListChanged);
    void deleteAll(List<Task> taskListDeleted);


    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState taskState, Context context);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);

    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(long lowerOrderIdTaskState, long higherOrderIdTaskState, TaskState taskState, Context context);

  void moveTasksUpByTaskState(Task sourceTask, Task destinationTask);

  void moveTasksDownByTaskState(Task sourceTask, Task destinationTask);

}
