package org.woehlke.simpleworklist.entities.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.Project;
import org.woehlke.simpleworklist.entities.entities.Task;
import org.woehlke.simpleworklist.entities.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);

    Page<Task> findByProject(Project thisProject, Pageable pageable);

    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);

    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    List<Task> findByContext(Context context);

    Page<Task> findByUserAccountAndContext(UserAccount userAccount, Context context, Pageable request);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState inbox, Context context);

    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    Page<Task> findByTaskStateAndContext(TaskState inbox, Context context, Pageable request);

    List<Task> findByTaskStateAndContext(TaskState trashed, Context context);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskState(TaskState taskState, Context context);

    @Query("select t from Task t where t.orderIdTaskState > :lowerTask and t.orderIdTaskState < :higherTask"
                + " and t.taskState = :taskState and t.context = :context")
    List<Task> getTasksToReorderByOrderIdTaskState(
        @Param("lowerTask") long lowerTaskId,
        @Param("higherTask") long higherTaskId,
        @Param("taskState") TaskState taskState,
        @Param("context") Context context
    );

    @Query("select t from Task t where t.orderIdProject > :lowerTask and t.orderIdProject < :higherTask"
            + " and t.project = :project and t.context = :context ")
    List<Task> getTasksToReorderByOrderIdProject(
        @Param("lowerTask") long lowerTaskId,
        @Param("higherTask") long higherTaskId,
        @Param("project") Project project,
        @Param("context") Context context
    );

}
