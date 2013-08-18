package org.woehlke.simpleworklist.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

    List<ActionItem> findByCategory(Category thisCategory);

    Page<ActionItem> findByCategoryIsNull(Pageable pageable);

    Page<ActionItem> findByCategory(Category thisCategory, Pageable pageable);
}
