package org.woehlke.simpleworklist.services.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.ActionItem;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.repository.ActionItemRepository;
import org.woehlke.simpleworklist.services.ActionItemService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ActionItemServiceImpl implements ActionItemService {

    @Inject
    private ActionItemRepository actionItemRepository;

    @Override
    public Page<ActionItem> findByCategory(Category thisCategory,
                                     Pageable request) {
        return actionItemRepository.findByCategory(thisCategory, request);
    }

    @Override
    public Page<ActionItem> findByRootCategory(Pageable request) {
        return actionItemRepository.findByCategoryIsNull(request);
    }

    @Override
    public ActionItem findOne(long dataId) {
        return actionItemRepository.findOne(dataId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public ActionItem saveAndFlush(ActionItem entity) {
        return actionItemRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(ActionItem actionItem) {
        actionItemRepository.delete(actionItem);
    }

    @Override
    public boolean categoryHasNoData(Category category) {
        return actionItemRepository.findByCategory(category).isEmpty();
    }

}
