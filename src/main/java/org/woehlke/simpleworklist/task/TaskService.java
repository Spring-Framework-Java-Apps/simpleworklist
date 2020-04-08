package org.woehlke.simpleworklist.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;

public interface TaskService {

    boolean projectHasNoTasks(Project project);

    Page<Task> getEmptyPage(Pageable request);

    Page<Task> findByProject(Project thisProject, Pageable request);

    Page<Task> findByRootProject(Context context, Pageable request);

    Task findOne(long taskId);

    Task addToInbox(Task task);
    Task addToRootProject(Task task);

    Task updatedViaTaskstate(Task task);
    Task updatedViaProject(Task task);

}
