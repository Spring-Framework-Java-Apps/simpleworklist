package org.woehlke.simpleworklist.oodm.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Task;
import org.woehlke.simpleworklist.oodm.entities.Project;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;

public interface TaskService {

    Task saveAndFlush(Task persistentTask);

    void delete(Task task);

    boolean projectHasNoTasks(Project project);

    void undelete(Task task);

    void complete(Task task);

    void incomplete(Task task);

    void setFocus(Task task);

    void unsetFocus(Task task);

    Page<Task> findByProject(Project thisProject, Context context, Pageable request);

    Page<Task> findByRootProject(Context context,Pageable request);

    Page<Task> findByUser(UserAccount thisUserAccount, Pageable request);

    Task findOne(long taskId);
}
