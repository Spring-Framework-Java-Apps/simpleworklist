package org.woehlke.simpleworklist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.UserMessage;

import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    String JQL = "select m from UserMessage m where (m.sender = :thisUser and m.receiver = :otherUser) or (m.sender = :otherUser and m.receiver = :thisUser) order by m.createdTimestamp desc";

    @Query(JQL)
    Page<UserMessage> findFirst20MessagesBetweenCurrentAndOtherUser(@Param("thisUser") UserAccount thisUser, @Param("otherUser") UserAccount otherUser, Pageable pageRequest);

    @Query(JQL)
    List<UserMessage> findAllMessagesBetweenCurrentAndOtherUser(@Param("thisUser") UserAccount thisUser, @Param("otherUser") UserAccount otherUser);

    List<UserMessage> findByReceiverAndReadByReceiver(UserAccount receiver,boolean readByReceiver);

    List<UserMessage> findBySenderAndReceiverAndReadByReceiver(UserAccount sender, UserAccount receiver, boolean readByReceiver);
}
