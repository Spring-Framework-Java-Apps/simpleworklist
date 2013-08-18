package org.woehlke.simpleworklist.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;

public interface ActionItemService {

    Page<ActionItem> findByCategory(Category thisCategory, Pageable request);

    Page<ActionItem> findByRootCategory(Pageable request);

    ActionItem findOne(long dataId);

    ActionItem saveAndFlush(ActionItem persistentActionItem);

    void delete(ActionItem actionItem);

    boolean categoryHasNoData(Category category);

}
