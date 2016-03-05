package org.woehlke.simpleworklist.services;

import java.util.List;

import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface ProjectService {

    List<Project> getBreadcrumb(Project thisProject, UserAccount user);

    List<Project> findRootProjectsByUserAccount(UserAccount userAccount);

    List<Project> findAllProjectsByUserAccount(UserAccount user);

    void moveProjectToAnotherProject(Project thisProject, Project targetProject, UserAccount user);

    Project findByProjectId(long categoryId, UserAccount user);

    Project saveAndFlush(Project project, UserAccount user);

    void delete(Project project, UserAccount user);
}
