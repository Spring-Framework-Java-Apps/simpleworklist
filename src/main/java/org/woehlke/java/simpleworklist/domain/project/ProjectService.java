package org.woehlke.java.simpleworklist.domain.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.java.simpleworklist.domain.context.Context;

public interface ProjectService {

    List<Project> findRootProjectsByContext(Context context);
    Page<Project> findRootProjectsByContext(Context context, Pageable pageRequest);

    List<Project> findAllProjectsByContext(Context context);
    Page<Project> findAllProjectsByContext(Context context, Pageable pageRequest);

    Project moveProjectToAnotherProject(Project thisProject, Project targetProject);

    Project findByProjectId(long projectId);

    Project add(Project project);

    Project update(Project project);

    Project delete(Project project);

    Project moveProjectToAnotherContext(Project thisProject, Context newContext);

}
