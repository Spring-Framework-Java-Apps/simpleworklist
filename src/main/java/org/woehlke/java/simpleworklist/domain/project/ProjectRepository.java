package org.woehlke.java.simpleworklist.domain.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.context.Context;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByContext(Context context);
    Page<Project> findByContext(Context context, Pageable pageRequest);

    List<Project> findByParentIsNullAndContext(Context context);
    Page<Project> findByParentIsNullAndContext(Context context, Pageable pageRequest);

}