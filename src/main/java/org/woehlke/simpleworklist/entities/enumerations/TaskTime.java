package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskTime {

    @Enumerated
    MIN5,

    @Enumerated
    MIN10,

    @Enumerated
    MIN15,

    @Enumerated
    MIN30,

    @Enumerated
    MIN45,

    @Enumerated
    HOUR1,

    @Enumerated
    HOUR2,

    @Enumerated
    HOUR3,

    @Enumerated
    HOUR4,

    @Enumerated
    HOUR6,

    @Enumerated
    HOUR8,

    @Enumerated
    MORE,

    @Enumerated
    NONE

}
