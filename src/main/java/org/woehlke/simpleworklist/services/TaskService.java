package org.woehlke.simpleworklist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;

public interface TaskService {

    Page<Task> findByProject(Project thisProject, Pageable request, UserAccount userAccount);

    Page<Task> findByRootProject(Pageable request, UserAccount userAccount);

    Task findOne(long dataId, UserAccount userAccount);

    Task saveAndFlush(Task persistentTask, UserAccount userAccount);

    void delete(Task task, UserAccount userAccount);

    boolean projectHasNoTasks(Project project, UserAccount userAccount);

    void undelete(Task task, UserAccount userAccount);

    void emptyTrash(UserAccount userAccount);

    void complete(Task task, UserAccount userAccount);

    void incomplete(Task task, UserAccount userAccount);

    void setFocus(Task task, UserAccount userAccount);

    void unsetFocus(Task task, UserAccount userAccount);

    Page<Task> findByProject(Project thisProject, Pageable request, UserAccount userAccount, Context context);

    Page<Task> findByRootProject(Pageable request, UserAccount userAccount, Context context);

    Page<Task> findByUser(UserAccount userAccount, Pageable request);

    long getMaxOrderIdTaskState(TaskState inbox, Context context, UserAccount thisUser);

    long getMaxOrderIdProject(Project project, Context context, UserAccount userAccount);

    void moveOrderIdTaskState(Task sourceTask, Task destinationTask);

    void moveOrderIdProject(Task sourceTask, Task destinationTask);

}
