package org.woehlke.simpleworklist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface TaskService {

    Page<Task> findByProject(Project thisProject, Pageable request);

    Page<Task> findByRootProject(Pageable request, UserAccount userAccount);

    Task findOne(long dataId);

    Task saveAndFlush(Task persistentTask);

    void delete(Task task);

    boolean projectHasNoTasks(Project project);

    void undelete(Task task);

    void emptyTrash(UserAccount userAccount);

    void complete(Task task);

    void incomplete(Task task);
}
