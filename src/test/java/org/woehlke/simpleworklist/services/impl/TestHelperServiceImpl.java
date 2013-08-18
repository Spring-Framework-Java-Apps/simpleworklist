package org.woehlke.simpleworklist.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.Category;
import org.woehlke.simpleworklist.repository.*;
import org.woehlke.simpleworklist.services.TestHelperService;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TestHelperServiceImpl implements TestHelperService {

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private ActionItemRepository actionItemRepository;

    @Inject
    private RegistrationProcessRepository registrationProcessRepository;

    @Inject
    private TimelineYearRepository timelineYearRepository;

    @Inject
    private TimelineMonthRepository timelineMonthRepository;

    @Inject
    private TimelineDayRepository timelineDayRepository;

    @Inject
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllRegistrationProcess() {
        registrationProcessRepository.deleteAll();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllActionItem() {
        actionItemRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteAllCategory() {
        List<Category> roots = categoryRepository.findByParentIsNull();
        for(Category root:roots){
            remove(root);
        }
    }

    private void remove(Category root){
        for(Category child:root.getChildren()){
            remove(child);
        }
        categoryRepository.delete(root);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteUserAccount() {
        userAccountRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteTimelineDay() {
        timelineDayRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteTimelineMonth() {
        timelineMonthRepository.deleteAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteTimelineYear() {
        timelineYearRepository.deleteAll();
    }

    @Override
    public int getNumberOfAllRegistrations() {
        return registrationProcessRepository.findAll().size();
    }
}
