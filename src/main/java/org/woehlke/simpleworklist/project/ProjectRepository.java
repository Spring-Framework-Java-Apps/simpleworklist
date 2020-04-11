package org.woehlke.simpleworklist.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.context.Context;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByContext(Context context);

    List<Project> findByParentIsNullAndContext(Context context);

}
