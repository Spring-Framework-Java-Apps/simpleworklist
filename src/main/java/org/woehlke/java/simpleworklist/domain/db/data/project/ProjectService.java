package org.woehlke.java.simpleworklist.domain.db.data.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.db.data.Project;

import java.util.List;

public interface ProjectService {

    Project add(Project project);
    Project update(Project project);
    Project delete(Project project);

    List<Project> findRootProjectsByContext(Context context);
    Page<Project> findRootProjectsByContext(Context context, Pageable pageRequest);

    List<Project> findAllProjectsByContext(Context context);
    Page<Project> findAllProjectsByContext(Context context, Pageable pageRequest);

    Project moveProjectToAnotherProject(Project thisProject, Project targetProject);
    Project moveProjectToAnotherContext(Project thisProject, Context newContext);

    Project getReferenceById(long projectId);
    Project findByProjectId(long projectId);
}
