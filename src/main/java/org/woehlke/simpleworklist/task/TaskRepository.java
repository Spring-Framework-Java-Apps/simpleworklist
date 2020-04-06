package org.woehlke.simpleworklist.task;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.taskstate.TaskState;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);

    Page<Task> findByProject(Project thisProject, Pageable pageable);

    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);

    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    List<Task> findByContext(Context context);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState inbox, Context context);

    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    Page<Task> findByTaskStateAndContext(TaskState taskState, Context context, Pageable request);

    List<Task> findByTaskStateAndContext(TaskState taskState, Context context);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(TaskState taskState, Context context);

    @Query("select t from Task t"
            + " where t.orderIdTaskState > :lowerOrderIdTaskState and t.orderIdTaskState < :higherOrderIdTaskState"
            + " and t.taskState = :taskState and t.context = :context")
    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context
    );

    @Query("select t from Task t"
            + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
            + " and t.project = :project and t.context = :context ")
    List<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project,
        @Param("context") Context context
    );

}
