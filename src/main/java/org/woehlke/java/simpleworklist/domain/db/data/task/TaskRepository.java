package org.woehlke.java.simpleworklist.domain.db.data.task;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;
import org.woehlke.java.simpleworklist.domain.db.data.Context;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByContext(Context context);

    @Query(name="findByTaskStateInbox")
    Page<Task> findByTaskStateInbox(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateInbox")
    List<Task> findByTaskStateInbox(@Param("context") Context context);

    @Query(name="findByTaskStateToday")
    Page<Task> findByTaskStateToday(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateToday")
    List<Task> findByTaskStateToday(@Param("context") Context context);

    @Query(name="findByTaskStateNext")
    Page<Task> findByTaskStateNext(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateNext")
    List<Task> findByTaskStateNext(@Param("context") Context context);

    @Query(name="findByTaskStateWaiting")
    Page<Task> findByTaskStateWaiting(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateWaiting")
    List<Task> findByTaskStateWaiting(@Param("context") Context context);

    @Query(name="findByTaskStateScheduled")
    Page<Task> findByTaskStateScheduled(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateScheduled")
    List<Task> findByTaskStateScheduled(@Param("context") Context context);

    @Query(name="findByTaskStateSomeday")
    Page<Task> findByTaskStateSomeday(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateSomeday")
    List<Task> findByTaskStateSomeday(@Param("context") Context context);

    Page<Task> findByFocusIsTrue(Context context, Pageable request);
    List<Task> findByFocusIsTrue(Context context);

    @Query(name="findByTaskStateCompleted")
    Page<Task> findByTaskStateCompleted(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateCompleted")
    List<Task> findByTaskStateCompleted(@Param("context") Context context);

    @Query(name="findByTaskStateTrash")
    Page<Task> findByTaskStateTrash(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateTrash")
    List<Task> findByTaskStateTrash(@Param("context") Context context);

    @Query(name="findByTaskStateDeleted")
    Page<Task> findByTaskStateDeleted(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateDeleted")
    List<Task> findByTaskStateDeleted(@Param("context") Context context);

    @Query(name="findByTaskStateProjects")
    Page<Task> findByTaskStateProjects(@Param("context") Context context, Pageable request);
    @Query(name="findByTaskStateProjects")
    List<Task> findByTaskStateProjects(@Param("context") Context context);


    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState taskState, Context context);
    List<Task> findByProject(Project thisProject);
    Page<Task> findByProject(Project thisProject, Pageable pageable);
    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    @Query(name="queryGetTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context
    );

    @Query(name="queryGetTasksByOrderIdProjectBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project
    );

    @Query(name = "queryGetTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("context") Context context
    );

}
