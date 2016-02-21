package org.woehlke.simpleworklist.services;

import java.util.List;

import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface ProjectService {

    List<Project> getBreadcrumb(Project thisProject);

    List<Project> findRootCategoriesByUserAccount(UserAccount userAccount);

    List<Project> findAllByUserAccount(UserAccount user);

    void moveCategoryToAnotherCategory(Project thisProject, Project targetProject);

    Project findByCategoryId(long categoryId);

    Project saveAndFlush(Project project);

    void delete(Project project);
}
