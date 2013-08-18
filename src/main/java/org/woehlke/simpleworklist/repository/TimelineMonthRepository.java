package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.TimelineMonth;
import org.woehlke.simpleworklist.entities.TimelineYear;

public interface TimelineMonthRepository extends JpaRepository<TimelineMonth, Long> {

    TimelineMonth findByYearAndMonthOfYear(TimelineYear timelineYear, int month);
}
