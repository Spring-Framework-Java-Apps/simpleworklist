package org.woehlke.simpleworklist.entities.services;

import java.util.List;

import org.woehlke.simpleworklist.entities.entities.Context;
import org.woehlke.simpleworklist.entities.entities.Project;
import org.woehlke.simpleworklist.entities.entities.UserAccount;

public interface ProjectService {

    List<Project> getBreadcrumb(Project thisProject, UserAccount user);

    List<Project> findRootProjectsByUserAccount(UserAccount userAccount);

    List<Project> findAllProjectsByUserAccount(UserAccount user);

    void moveProjectToAnotherProject(Project thisProject, Project targetProject, UserAccount user);

    Project findByProjectId(long categoryId, UserAccount user);

    Project saveAndFlush(Project project, UserAccount user);

    void delete(Project project, UserAccount user);

    List<Project> findAllProjectsByUserAccountAndContext(UserAccount user, Context context);

    List<Project> findRootProjectsByUserAccountAndContext(UserAccount user, Context context);

    void moveProjectToAnotherContext(Project thisProject, Context newContext, UserAccount userAccount);

}
