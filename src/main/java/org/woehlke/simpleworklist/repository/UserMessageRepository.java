package org.woehlke.simpleworklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woehlke.simpleworklist.entities.UserMessage;

/**
 * Created by Fert on 16.02.2016.
 */
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
}
