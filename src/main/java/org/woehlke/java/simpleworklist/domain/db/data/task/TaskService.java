package org.woehlke.java.simpleworklist.domain.db.data.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;

import java.util.List;

public interface TaskService {

    Task findById(long taskId);

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

    Page<Task> findByProjectId(Project thisProject, Pageable request);
    Page<Task> findByProjectRoot(Context context, Pageable request);

    boolean projectHasNoTasks(Project project);

    Task saveAndFlush(Task task);
    void saveAll(List<Task> taskListChanged);
    void deleteAll(List<Task> taskListDeleted);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState taskState, Context context);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);

    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(long lowerOrderIdTaskState, long higherOrderIdTaskState, TaskState taskState, Context context);
    List<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(long lowerOrderIdProject, long higherOrderIdProject, Context context);
    List<Task> getTasksByOrderIdProjectIdBetweenLowerTaskAndHigherTask(long lowerOrderIdProject, long higherOrderIdProject, Project project);

}
