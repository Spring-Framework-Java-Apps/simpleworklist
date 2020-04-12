package org.woehlke.simpleworklist.user.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.List;

/**
 * Created by Fert on 16.02.2016.
 */
@Repository
public interface User2UserMessageRepository extends JpaRepository<User2UserMessage, Long> {

    //TODO: move the JQL Query-String to Entity as Prepared Statement
    String JQL = "select m from User2UserMessage m "
    + "where (m.sender = :thisUser and m.receiver = :otherUser) "
    + "or (m.sender = :otherUser and m.receiver = :thisUser)";

    @Query(JQL)
    Page<User2UserMessage> findAllMessagesBetweenCurrentAndOtherUser(
            @Param("thisUser") UserAccount thisUser,
            @Param("otherUser") UserAccount otherUser,
            Pageable request
    );

    //TODO:  change List<Project> to Page<Project>
    @Deprecated
    List<User2UserMessage> findByReceiverAndReadByReceiver(
            UserAccount receiver,
            boolean readByReceiver
    );
    Page<User2UserMessage> findByReceiverAndReadByReceiver(
        UserAccount receiver,
        boolean readByReceiver,
        Pageable request
    );

    //TODO: change List<User2UserMessage> to Page<User2UserMessage>
    @Deprecated
    List<User2UserMessage> findBySenderAndReceiverAndReadByReceiver(
            UserAccount sender, UserAccount receiver, boolean readByReceiver
    );
    Page<User2UserMessage> findBySenderAndReceiverAndReadByReceiver(
        UserAccount sender, UserAccount receiver, boolean readByReceiver, Pageable request
    );
}
