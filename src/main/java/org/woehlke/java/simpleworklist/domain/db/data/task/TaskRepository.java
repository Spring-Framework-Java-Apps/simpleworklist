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
import org.woehlke.java.simpleworklist.domain.meso.taskworkflow.TaskState;
import org.woehlke.java.simpleworklist.domain.db.data.Context;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);
    Page<Task> findByProject(Project thisProject, Pageable pageable);
    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);
    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    List<Task> findByContext(Context context);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    List<Task> findByTaskStateAndContext(TaskState taskState, Context context);
    Page<Task> findByTaskStateAndContext(TaskState taskState, Context context, Pageable request);

    @Query(name="findByTaskStateTrashAndContext")
    Page<Task> findByTaskStateTrashAndContext(@Param("context") Context context, Pageable request);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
        TaskState taskState, Context context
    );

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
