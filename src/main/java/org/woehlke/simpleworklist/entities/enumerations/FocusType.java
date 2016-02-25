package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

/**
 * Created by tw on 21.02.16.
 */
public enum FocusType {

    @Enumerated
    INBOX,

    @Enumerated
    TODAY,

    @Enumerated
    NEXT,

    @Enumerated
    SCHEDULED,

    @Enumerated
    SOMEDAY,

    @Enumerated
    COMPLETED,

    @Enumerated
    TRASHED
}
