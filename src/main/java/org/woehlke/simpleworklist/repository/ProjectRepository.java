package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.UserAccount;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByParentIsNull();

    List<Project> findByParentIsNullAndUserAccount(UserAccount userAccount);

    List<Project> findByUserAccount(UserAccount userAccount);
}
