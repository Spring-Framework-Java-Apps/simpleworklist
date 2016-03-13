package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findByUserAccount(UserAccount user);

    Area findByIdAndUserAccount(long newAreaId, UserAccount userAccount);

}
