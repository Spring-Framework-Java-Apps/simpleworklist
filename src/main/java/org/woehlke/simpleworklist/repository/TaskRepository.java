package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.Project;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.FocusType;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project thisProject);

    Page<Task> findByProjectIsNull(Pageable pageable);

    Page<Task> findByProject(Project thisProject, Pageable pageable);

    Page<Task> findByFocusTypeAndUserAccount(FocusType focusType, UserAccount thisUser, Pageable request);

}
