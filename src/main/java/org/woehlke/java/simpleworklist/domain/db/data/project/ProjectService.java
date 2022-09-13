package org.woehlke.java.simpleworklist.domain.db.data.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;
import org.woehlke.java.simpleworklist.domain.db.data.Task;

import java.util.List;

public interface ProjectService {

    Project add(Project project);
    Project update(Project project);
    void delete(Project project);


    List<Task> findByProject(Project thisProject);

    List<Project> findRootProjectsByContext(Context context);
    Page<Project> findRootProjectsByContext(Context context, Pageable pageRequest);

    List<Project> findAllProjectsByContext(Context context);
    Page<Project> findAllProjectsByContext(Context context, Pageable pageRequest);

    Project getReferenceById(long projectId);
    Project findByProjectId(long projectId);


    List<Project> getAllChildrenOfProject(Project thisProject);
}
