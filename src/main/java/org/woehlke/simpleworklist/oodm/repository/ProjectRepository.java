package org.woehlke.simpleworklist.oodm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.oodm.entities.Context;
import org.woehlke.simpleworklist.oodm.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByParentIsNull();

    //@Deprecated
    //List<Project> findByParentIsNullAndUserAccount(UserAccount userAccount);

    //@Deprecated
    //List<Project> findByUserAccount(UserAccount userAccount);

    List<Project> findByContext(Context context);

    List<Project> findByParentIsNullAndContext(Context context);

}
