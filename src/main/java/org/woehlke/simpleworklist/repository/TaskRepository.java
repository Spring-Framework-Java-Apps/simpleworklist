package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);

    Page<Task> findByProjectIsNullAndUserAccount(Pageable pageable, UserAccount userAccount);

    Page<Task> findByProject(Project thisProject, Pageable pageable);

    Page<Task> findByTaskStateAndUserAccount(TaskState taskState, UserAccount thisUser, Pageable request);

    List<Task> findByTaskStateAndUserAccount(TaskState taskState, UserAccount userAccount);

    Page<Task> findByFocusAndContext(boolean focus, Context context, Pageable request);

    Page<Task> findByProjectIsNullAndContext(Context context, Pageable request);

    List<Task> findByContext(Context context);

    Page<Task> findByUserAccount(UserAccount userAccount, Pageable request);

    Task findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(TaskState inbox, Context context);

    Task findTopByProjectAndContextOrderByOrderIdProjectDesc(Project project, Context context);

    Page<Task> findByFocusAndUserAccount(boolean focus, UserAccount thisUser, Pageable request);

    Page<Task> findByTaskStateAndContext(TaskState inbox, Context context, Pageable request);

    List<Task> findByTaskStateAndContext(TaskState trashed, Context context);

    List<Task> findByTaskStateAndUserAccountOrderByOrderIdTaskState(TaskState completed, UserAccount thisUser);

    List<Task> findByTaskStateAndContextOrderByOrderIdTaskState(TaskState completed, Context context);

    @Query("select t from Task t where t.orderIdTaskState > :lowerTask and t.orderIdTaskState < :higherTask ")
    List<Task> getTasksToReorderByOrderIdTaskState(@Param("lowerTask") long lowerTaskId, @Param("higherTask") long higherTaskId);

    @Query("select t from Task t where t.orderIdProject > :lowerTask and t.orderIdProject < :higherTask ")
    List<Task> getTasksToReorderByOrderIdProject(@Param("lowerTask") long lowerTaskId, @Param("higherTask") long higherTaskId);

}
