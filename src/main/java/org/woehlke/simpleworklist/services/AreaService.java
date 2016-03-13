package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.entities.Area;
import org.woehlke.simpleworklist.entities.UserAccount;

import java.util.List;

/**
 * Created by tw on 13.03.16.
 */
public interface AreaService {

    List<Area> getAllForUser(UserAccount user);

    Area findByIdAndUserAccount(long newAreaId, UserAccount userAccount);
}
