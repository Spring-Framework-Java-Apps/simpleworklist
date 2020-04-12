package org.woehlke.simpleworklist.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.context.Context;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    List<Project> findByContext(Context context);
    Page<Project> findByContext(Context context, Pageable pageRequest);

    //TODO: #245 change List<Project> to Page<Project>
    @Deprecated
    List<Project> findByParentIsNullAndContext(Context context);
    Page<Project> findByParentIsNullAndContext(Context context, Pageable pageRequest);

}
