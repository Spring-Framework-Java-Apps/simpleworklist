package org.woehlke.simpleworklist.entities.enumerations;

import javax.persistence.Enumerated;

/**
 * Created by tw on 08.03.16.
 */
public enum TaskEnergy {

    @Enumerated
    LOW,

    @Enumerated
    MEDIUM,

    @Enumerated
    HIGH,

    @Enumerated
    NONE
}
