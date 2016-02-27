package org.woehlke.simpleworklist.services;

import java.util.List;

import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface ProjectService {

    List<Project> getBreadcrumb(Project thisProject);

    List<Project> findRootProjectsByUserAccount(UserAccount userAccount);

    List<Project> findAllProjectsByUserAccount(UserAccount user);

    void moveProjectToAnotherProject(Project thisProject, Project targetProject);

    Project findByProjectId(long categoryId);

    Project saveAndFlush(Project project);

    void delete(Project project);
}
