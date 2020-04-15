package org.woehlke.simpleworklist.domain.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.project.Project;

public interface ProjectService {

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    List<Project> findRootProjectsByContext(Context context);
    Page<Project> findRootProjectsByContext(Context context, Pageable pageRequest);

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    List<Project> findAllProjectsByContext(Context context);
    Page<Project> findAllProjectsByContext(Context context, Pageable pageRequest);

    Project moveProjectToAnotherProject(Project thisProject, Project targetProject);

    Project findByProjectId(long projectId);

    Project add(Project project);

    Project update(Project project);

    Project delete(Project project);

    Project moveProjectToAnotherContext(Project thisProject, Context newContext);

}
