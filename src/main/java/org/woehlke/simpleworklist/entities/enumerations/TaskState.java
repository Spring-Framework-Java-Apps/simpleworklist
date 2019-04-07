package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Created by tw on 21.02.16.
 */
public enum TaskState implements Serializable {

    @Enumerated
    INBOX,

    @Enumerated
    TODAY,

    @Enumerated
    NEXT,

    @Enumerated
    WAITING,

    @Enumerated
    SCHEDULED,

    @Enumerated
    SOMEDAY,

    @Enumerated
    COMPLETED,

    @Enumerated
    TRASHED
}
