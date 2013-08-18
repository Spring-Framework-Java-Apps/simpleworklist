package org.woehlke.simpleworklist.services.impl;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.TimelineDay;
import org.woehlke.simpleworklist.entities.TimelineMonth;
import org.woehlke.simpleworklist.entities.TimelineYear;
import org.woehlke.simpleworklist.repository.TimelineDayRepository;
import org.woehlke.simpleworklist.repository.TimelineMonthRepository;
import org.woehlke.simpleworklist.repository.TimelineYearRepository;
import org.woehlke.simpleworklist.services.TimelineService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TimelineServiceImpl implements TimelineService {

    @Inject
    private TimelineYearRepository timelineYearRepository;

    @Inject
    private TimelineMonthRepository timelineMonthRepository;

    @Inject
    private TimelineDayRepository timelineDayRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public TimelineDay getTodayFactory() {
        Date now = new Date();
        int year = now.getYear();
        int month = now.getMonth();
        int day = now.getDay();
        TimelineYear timelineYear = timelineYearRepository.findByYear(year);
        if (timelineYear == null) {
            timelineYear = new TimelineYear();
            timelineYear.setYear(year);
            timelineYear = timelineYearRepository.saveAndFlush(timelineYear);
        }
        TimelineMonth timelineMonth = timelineMonthRepository.findByYearAndMonthOfYear(timelineYear, month);
        if (timelineMonth == null) {
            timelineMonth = new TimelineMonth();
            timelineMonth.setMonthOfYear(month);
            timelineMonth.setYear(timelineYear);
            timelineMonth = timelineMonthRepository.saveAndFlush(timelineMonth);
        }
        TimelineDay timelineDay = timelineDayRepository.findByMonthAndDayOfMonth(timelineMonth, day);
        if (timelineDay == null) {
            timelineDay = new TimelineDay();
            timelineDay.setDayOfMonth(day);
            timelineDay.setMonth(timelineMonth);
            timelineDay = timelineDayRepository.saveAndFlush(timelineDay);
        }
        return timelineDay;
    }

}
