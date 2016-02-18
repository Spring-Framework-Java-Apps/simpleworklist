package org.woehlke.simpleworklist.repository;

import org.springframework.data.domain.Pageable;
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

    String JQL = "select m from UserMessage m where (m.sender = ?1 and m.receiver = ?2) or (m.sender = ?2 and m.receiver = ?1) order by m.createdTimestamp desc";

    @Query(JQL)
    List<UserMessage> findFirst20MessagesBetweenCurrentAndOtherUser(UserAccount thisUser, UserAccount otherUser, Pageable pageRequest);

    @Query(JQL)
    List<UserMessage> findAllMessagesBetweenCurrentAndOtherUser(UserAccount sender, UserAccount receiver);

    List<UserMessage> findByReceiverAndReadByReceiver(UserAccount receiver,boolean readByReceiver);

    List<UserMessage> findBySenderAndReceiverAndReadByReceiver(UserAccount sender, UserAccount receiver, boolean readByReceiver);
}
