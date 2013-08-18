package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.TimelineDay;
import org.woehlke.simpleworklist.entities.TimelineMonth;

public interface TimelineDayRepository extends JpaRepository<TimelineDay, Long> {

    TimelineDay findByMonthAndDayOfMonth(TimelineMonth timelineMonth, int day);
}
