package org.woehlke.simpleworklist.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Context;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;

public interface TaskService {

    Task findOne(long dataId, UserAccount userAccount);

    Task saveAndFlush(Task persistentTask, UserAccount userAccount);

    void delete(Task task, UserAccount userAccount);

    boolean projectHasNoTasks(Project project, UserAccount userAccount);

    void undelete(Task task, UserAccount userAccount);

    void complete(Task task, UserAccount userAccount);

    void incomplete(Task task, UserAccount userAccount);

    void setFocus(Task task, UserAccount userAccount);

    void unsetFocus(Task task, UserAccount userAccount);

    Page<Task> findByProject(Project thisProject, Pageable request, UserAccount userAccount, Context context);

    Page<Task> findByRootProject(Pageable request, UserAccount userAccount, Context context);

    Page<Task> findByUser(UserAccount userAccount, Context context, Pageable request);

    long getMaxOrderIdTaskState(TaskState taskState, Context context, UserAccount thisUser);

    long getMaxOrderIdProject(Project project, Context context, UserAccount userAccount);

    void moveOrderIdTaskState(TaskState taskState, Task sourceTask, Task destinationTask, Context context);

    void moveOrderIdProject(Project project, Task sourceTask, Task destinationTask, Context context);

}
