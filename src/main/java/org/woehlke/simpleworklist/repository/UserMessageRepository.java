package org.woehlke.simpleworklist.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;

import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    @Query("select m from UserMessage m where (m.sender = ?1 and m.receiver = ?2) or (m.sender = ?2 and m.receiver = ?1) order by m.createdTimestamp desc")
    List<UserMessage> findAllMessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser);
}
