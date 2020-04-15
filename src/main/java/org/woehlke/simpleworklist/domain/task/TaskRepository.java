package org.woehlke.simpleworklist.domain.task;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskState;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //TODO: #244 change List<Task> to Page<Task>
    @Deprecated
    List<Task> findByProject(Project thisProject);
    Page<Task> findByProject(Project thisProject, Pageable pageable);
    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);
    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    //TODO: #244 change List<Task> to Page<Task>
    @Deprecated
    List<Task> findByContext(Context context);
    Page<Task> findByContext(Context context, Pageable pageable);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    //TODO: #244 change List<Task> to Page<Task>>
    @Deprecated
    List<Task> findByTaskStateAndContext(TaskState taskState, Context context);
    Page<Task> findByTaskStateAndContext(TaskState taskState, Context context, Pageable request);

    //TODO: #244 change List<Task> to Page<Task>
    @Deprecated
    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
        TaskState taskState, Context context
    );
    Page<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
        TaskState taskState, Context context, Pageable request
    );

    //TODO: #244 change List<Task> to Page<Task>
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Deprecated
    @Query("select t from Task t"
            + " where t.orderIdTaskState > :lowerOrderIdTaskState and t.orderIdTaskState < :higherOrderIdTaskState"
            + " and t.taskState = :taskState and t.context = :context")
    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context
    );
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Query("select t from Task t"
        + " where t.orderIdTaskState > :lowerOrderIdTaskState and t.orderIdTaskState < :higherOrderIdTaskState"
        + " and t.taskState = :taskState and t.context = :context")
    Page<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context,
        Pageable request
    );

    //TODO: #244 change List<Task> to Page<Task>
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Deprecated
    @Query("select t from Task t"
            + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
            + " and t.project = :project")
    List<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project
    );
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Query("select t from Task t"
        + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
        + " and t.project = :project")
    Page<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project,
        Pageable request
    );

    //TODO: #244 move from List<Task> to Page<Task>
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Deprecated
    @Query("select t from Task t"
        + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
        + " and t.project is null and t.context = :context ")
    List<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("context") Context context
    );
    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Query("select t from Task t"
        + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
        + " and t.project is null and t.context = :context ")
    Page<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("context") Context context,
        Pageable request
    );

}
