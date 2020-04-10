package org.woehlke.simpleworklist.project;

import java.util.List;

import org.woehlke.simpleworklist.context.Context;

public interface ProjectService {

    List<Project> findRootProjectsByContext(Context context);

    List<Project> findAllProjectsByContext(Context context);

    Project moveProjectToAnotherProject(Project thisProject, Project targetProject);

    Project findByProjectId(long projectId);

    Project add(Project project);

    Project update(Project project);

    Project delete(Project project);

    Project moveProjectToAnotherContext(Project thisProject, Context newContext);

}
