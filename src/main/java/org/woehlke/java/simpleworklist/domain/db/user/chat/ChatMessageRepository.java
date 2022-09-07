package org.woehlke.java.simpleworklist.domain.db.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.java.simpleworklist.domain.db.user.UserAccount;
import org.woehlke.java.simpleworklist.domain.db.user.UserChatMessage;

import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<UserChatMessage, Long> {

    @Query(name="queryFindAllMessagesBetweenCurrentAndOtherUser")
    Page<UserChatMessage> findAllMessagesBetweenCurrentAndOtherUser(
            @Param("thisUser") UserAccount thisUser,
            @Param("otherUser") UserAccount otherUser,
            Pageable request
    );

    List<UserChatMessage> findByReceiverAndReadByReceiver(
            UserAccount receiver,
            boolean readByReceiver
    );

    List<UserChatMessage> findBySenderAndReceiverAndReadByReceiver(
            UserAccount sender, UserAccount receiver, boolean readByReceiver
    );

}
