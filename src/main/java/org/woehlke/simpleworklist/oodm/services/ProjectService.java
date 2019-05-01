package org.woehlke.simpleworklist.oodm.services;

import java.util.List;

import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;
//import org.woehlke.simpleworklist.oodm.entities.UserAccount;

public interface ProjectService {

    List<Project> findRootProjectsByContext(Context context);

    List<Project> findAllProjectsByContext(Context context);

    //@Deprecated
    //List<Project> findRootProjectsByUserAccount(UserAccount userAccount);

    //@Deprecated
    //List<Project> findAllProjectsByUserAccount(UserAccount user);

    void moveProjectToAnotherProject(Project thisProject, Project targetProject);

    Project findByProjectId(long categoryId);

    Project saveAndFlush(Project project);

    void delete(Project project);

    void moveProjectToAnotherContext(Project thisProject, Context newContext);

}
