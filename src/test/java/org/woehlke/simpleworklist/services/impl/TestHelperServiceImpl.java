package org.woehlke.simpleworklist.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.repository.*;
import org.woehlke.simpleworklist.services.TestHelperService;

import javax.inject.Inject;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TestHelperServiceImpl implements TestHelperService {

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private DataRepository dataRepository;

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
    public void deleteAll() {
        registrationProcessRepository.deleteAll();
        timelineDayRepository.deleteAll();
        timelineMonthRepository.deleteAll();
        timelineDayRepository.deleteAll();
        dataRepository.deleteAll();
        categoryRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Override
    public int getNumberOfAllRegistrations() {
        return registrationProcessRepository.findAll().size();
    }
}
