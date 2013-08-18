package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.TimelineYear;

public interface TimelineYearRepository extends JpaRepository<TimelineYear, Long> {

    TimelineYear findByYear(int year);
}
