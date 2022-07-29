package org.woehlke.simpleworklist.domain.task;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.taskworkflow.TaskState;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);
    Page<Task> findByProject(Project thisProject, Pageable pageable);
    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);
    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    List<Task> findByContext(Context context);
    Page<Task> findByContext(Context context, Pageable pageable);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState taskState, Context context);
    Task findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(Context context);
    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    List<Task> findByTaskStateAndContext(TaskState taskState, Context context);
    Page<Task> findByTaskStateAndContext(TaskState taskState, Context context, Pageable request);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
        TaskState taskState, Context context
    );
    Page<Task> findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
        TaskState taskState, Context context, Pageable request
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    //@Query("select t from Task t"
    //        + " where t.orderIdTaskState > :lowerOrderIdTaskState and t.orderIdTaskState < :higherOrderIdTaskState"
    //        + " and t.taskState = :taskState and t.context = :context")
    @Query(name="queryGetTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Query(name="queryGetTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask")
    Page<Task> getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdTaskState") long lowerOrderIdTaskState,
        @Param("higherOrderIdTaskState") long higherOrderIdTaskState,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context,
        Pageable request
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    @Query(name="queryGetTasksByOrderIdProjectBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    //@Query("select t from Task t"
    //    + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
    //    + " and t.project = :project")
    @Query(name="queryGetTasksByOrderIdProjectBetweenLowerTaskAndHigherTask")
    Page<Task> getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("project") Project project,
        Pageable request
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    //@Query("select t from Task t"
    //    + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
    //    + " and t.project is null and t.context = :context ")
    @Query(name = "queryGetTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask")
    List<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("context") Context context
    );

    //TODO: #249 move the JQL Query-String to Entity as Prepared Statement
    //@Query("select t from Task t"
    //    + " where t.orderIdProject > :lowerOrderIdProject and t.orderIdProject < :higherOrderIdProject"
    //    + " and t.project is null and t.context = :context ")
    @Query(name = "queryGetTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask")
    Page<Task> getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
        @Param("lowerOrderIdProject") long lowerOrderIdProject,
        @Param("higherOrderIdProject") long higherOrderIdProject,
        @Param("context") Context context,
        Pageable request
    );

}
